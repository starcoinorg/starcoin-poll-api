package org.starcoin.poll.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.novi.serde.DeserializationError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.starcoin.poll.api.bean.Event;
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

@Api(tags = {"投票列表配置接口"}, description = "投票列表配置接口，包含管理服务API")
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
        int status = contractService.getPollStatus(item.getCreator(), item.getTypeArgs1());
        item.setStatus(status);
        JSONObject votes = contractService.getPollVotes(item.getCreator());
        if (votes.containsKey("for_votes")) {
            item.setForVotes(votes.getLongValue("for_votes"));
        }
        if (votes.containsKey("against_votes")) {
            item.setAgainstVotes(votes.getLongValue("against_votes"));
        }
        return ResultUtils.success(item);
    }

    @ApiOperation(value = "获取配置列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "network", value = "网络参数", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "页码，默认第1页", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "count", value = "条数，默认为20条", dataType = "Integer", dataTypeClass = Integer.class)
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
            int status = contractService.getPollStatus(item.getCreator(), item.getTypeArgs1());
            item.setStatus(status);
            JSONObject votes = contractService.getPollVotes(item.getCreator());
            if (votes.containsKey("for_votes")) {
                item.setForVotes(votes.getLongValue("for_votes"));
            }
            if (votes.containsKey("against_votes")) {
                item.setAgainstVotes(votes.getLongValue("against_votes"));
            }
        }
        pageResult.setList(list);
        return ResultUtils.success(pageResult);
    }

    @ApiOperation(value = "添加配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "againstVotes", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "creator", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "description", value = "中文说明，前端js需用encodeURIComponent包装下", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "descriptionEn", value = "英文说明，前端js需用encodeURIComponent包装下", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "endTime", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "forVotes", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "link", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "title", value = "中文标题，前端js需用encodeURIComponent包装下", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "titleEn", value = "英文标题，前端js需用encodeURIComponent包装下", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "typeArgs1", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "status", value = "", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "network", value = "网络", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @PostMapping("/add")
    public Result addPollItem(Long againstVotes, String creator, String description, String descriptionEn, Long endTime, Long forVotes, String link, String title, String titleEn, String typeArgs1, Integer status, String network) {
        boolean result = pollItemService.add(againstVotes, creator, description, descriptionEn, endTime, forVotes, link, title, titleEn, typeArgs1, status, network);
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
            @ApiImplicitParam(name = "description", value = "中文说明，前端js需用encodeURIComponent包装下", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "descriptionEn", value = "英文说明，前端js需用encodeURIComponent包装下", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "endTime", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "forVotes", value = "", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "link", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "title", value = "中文标题，前端js需用encodeURIComponent包装下", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "titleEn", value = "英文标题，前端js需用encodeURIComponent包装下", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "typeArgs1", value = "", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "status", value = "", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "network", value = "网络", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @PostMapping("/modif")
    public Result modifPollItem(Long id, Long againstVotes, String creator, String description, String descriptionEn, Long endTime, Long forVotes, String link, String title, String titleEn, String typeArgs1, Integer status, String network) {
        boolean result = pollItemService.modif(id, againstVotes, creator, description, descriptionEn, endTime, forVotes, link, title, titleEn, typeArgs1, status, network);
        if (result) {
            return ResultUtils.success();
        }
        return ResultUtils.failure();
    }

    @ApiOperation(value = "删除配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @GetMapping("/del/{id}")
    public Result delPollItem(@PathVariable("id") Long id) {
        pollItemService.del(id);
        return ResultUtils.success();
    }

    @ApiOperation(value = "获取指定poll的详情列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "network", value = "网络参数", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "proposalId", value = "proposalId", required = true, dataType = "Long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "proposer", value = "proposer", required = true, dataType = "String", dataTypeClass = String.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @GetMapping("/votes/{network}/{proposalId}/{proposer}")
    public Result<List<Event>> delPollItem(@PathVariable("network") String network,
                                           @PathVariable("proposalId") Long proposalId,
                                           @PathVariable("proposer") String proposer) throws IOException, DeserializationError {
        List<Event> list = transactionService.getEventsByProposalIdAndProposer(network, proposalId, proposer);
        return ResultUtils.success(list);
    }
}
