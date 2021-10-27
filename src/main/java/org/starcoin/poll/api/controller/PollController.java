package org.starcoin.poll.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.novi.serde.DeserializationError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.starcoin.poll.api.bean.PollItem;
import org.starcoin.poll.api.service.ContractService;
import org.starcoin.poll.api.service.PollItemService;
import org.starcoin.poll.api.service.TransactionService;
import org.starcoin.poll.api.vo.PageResult;
import org.starcoin.poll.api.vo.Result;
import org.starcoin.poll.api.vo.ResultUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Api(tags = {"投票 API"})
@RestController
@RequestMapping("v1/polls")
public class PollController {

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @Resource
    private PollItemService pollItemService;

    @Resource
    private ContractService contractService;

    @Resource
    private TransactionService transactionService;

    @ApiOperation(value = "获取配置详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @GetMapping("/detail/{id}")
    public Result<PollItem> getPollItem(@PathVariable("id") Long id) {
        PollItem item = pollItemService.get(id);
        if (null == item) {
            return ResultUtils.failure();
        }
        updateByOnChainInfo(item, id);
        return ResultUtils.success(item);
    }

    @ApiOperation(value = "获取配置列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "network", value = "网络", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "页码，默认第1页", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "count", value = "每页记录条数，默认为 20 条", dataType = "Integer", dataTypeClass = Integer.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @GetMapping("/page/{network}")
    public Result<PageResult<PollItem>> getList(@PathVariable("network") String network, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "count", required = false, defaultValue = "20") int count) {
        if (page - 1 < 0) {
            page = 1;
        }
        PageResult<PollItem> pageResult = pollItemService.getListByNetwork(network, page - 1, count);
        List<PollItem> list = pageResult.getList();
        for (PollItem item : list) {
            updateByOnChainInfo(item, item.getId());
        }
        pageResult.setList(list);
        return ResultUtils.success(pageResult);
    }

    private void updateByOnChainInfo(PollItem pollItem, Long id) {
        if (pollItem.getEndTime().compareTo(System.currentTimeMillis()) > 0) {
            // 投票时间尚未结束，根据链上状态更新数据库中的 status
            try {
                Integer status = contractService.getPollStatus(pollItem.getIdOnChain(), pollItem.getCreator(), pollItem.getTypeArgs1());
                Integer oldStatus = pollItem.getStatus();
                pollItem.setStatus(status);
                if (!Objects.equals(oldStatus, status)) {
                    pollItemService.asyncUpdateStatus(id, status);
                }

                JSONObject pollObj = contractService.getPollVotes(pollItem.getCreator(), pollItem.getTypeArgs1());
                if (pollObj.containsKey("for_votes")) {
                    pollItem.setForVotes(pollObj.getLongValue("for_votes"));
                }
                if (pollObj.containsKey("against_votes")) {
                    pollItem.setAgainstVotes(pollObj.getLongValue("against_votes"));
                }
                if (pollObj.containsKey("quorum_votes")) {
                    pollItem.setQuorumVotes(pollObj.getLongValue("quorum_votes"));
                }
                if (pollObj.containsKey("start_time")) {
                    pollItem.setOnChainStartTime(pollObj.getLongValue("start_time"));
                }
                if (pollObj.containsKey("end_time")) {
                    pollItem.setOnChainEndTime(pollObj.getLongValue("end_time"));
                }
            } catch (RuntimeException e) {
                logger.error("Update poll status by on-chain info error.", e);
            }
        }
    }

    @ApiOperation(value = "添加配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "againstVotes", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "creator", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "description", value = "中文说明，注意可能需要 encodeURIComponent", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "descriptionEn", value = "英文说明，注意可能需要 encodeURIComponent", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "endTime", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "forVotes", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "link", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "title", value = "中文标题，注意可能需要 encodeURIComponent", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "titleEn", value = "英文标题，注意可能需要 encodeURIComponent", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "typeArgs1", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "status", value = "", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "network", value = "网络", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "idOnChain", value = "On-Chain proposal Id.", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @PostMapping("/add")
    public Result addPollItem(Long againstVotes, String creator, String description, String descriptionEn, Long endTime,
                              Long forVotes, String link, String title, String titleEn, String typeArgs1, Integer status,
                              String network, String idOnChain) {
        boolean result = pollItemService.add(againstVotes, creator, description, descriptionEn, endTime, forVotes,
                link, title, titleEn, typeArgs1, status, network, idOnChain);
        if (result) {
            return ResultUtils.success();
        }
        return ResultUtils.failure();
    }

    @ApiOperation(value = "修改配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "againstVotes", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "creator", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "description", value = "中文说明，注意可能需要 encodeURIComponent", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "descriptionEn", value = "英文说明，注意可能需要 encodeURIComponent", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "endTime", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "forVotes", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "link", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "title", value = "中文标题，注意可能需要 encodeURIComponent", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "titleEn", value = "英文标题，注意可能需要 encodeURIComponent", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "typeArgs1", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "status", value = "", required = true, dataType = "Integer", dataTypeClass = Integer.class) //,
            //@ApiImplicitParam(name = "network", value = "网络", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @PostMapping("/modif")
    public Result modifPollItem(Long id, Long againstVotes, String creator, String description, String descriptionEn,
                                Long endTime, Long forVotes, String link, String title, String titleEn, String typeArgs1,
                                Integer status
                                //, String network // 领域键（实体Id）应该也是不能修改的
    ) {
        boolean result = pollItemService.modif(id, againstVotes, creator, description, descriptionEn, endTime, forVotes,
                link, title, titleEn, typeArgs1, status);//, network);
        if (result) {
            return ResultUtils.success();
        }
        return ResultUtils.failure();
    }

    @ApiOperation(value = "删除投票配置信息（软删除）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @PostMapping("/del/{id}")
    public Result delPollItem(@PathVariable("id") Long id) {
        pollItemService.del(id);
        return ResultUtils.success();
    }

    @ApiOperation(value = "获取指定的投票事件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "network", value = "网络", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "proposalId", value = "proposal Id", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "proposer", value = "proposer", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @GetMapping("/votes/{network}/{proposalId}/{proposer}")
    public Result<List<JSONObject>> delPollItem(@PathVariable("network") String network,
                                                @PathVariable("proposalId") Long proposalId,
                                                @PathVariable("proposer") String proposer) throws IOException, DeserializationError {
        List<JSONObject> list = transactionService.getEventsByProposalIdAndProposer(network, proposalId, proposer);
        return ResultUtils.success(list);
    }
}
