package org.starcoin.poll.api.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.starcoin.poll.api.bean.PollItem;
import org.starcoin.poll.api.service.PollItemService;
import org.starcoin.poll.api.vo.PageResult;
import org.starcoin.poll.api.vo.Result;
import org.starcoin.poll.api.vo.ResultUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Api(tags = {"投票列表配置接口"}, description = "投票列表配置接口，包含管理服务API")
@RestController
@RequestMapping("v1/polls")
public class PollController {

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    private final RestTemplate restTemplate;

    @Resource
    private PollItemService pollItemService;

    @Autowired
    public PollController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @ApiOperation(value = "获取配置详情信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @GetMapping("/detail/{id}")
    public Result<PollItem> getPollItem(@PathVariable("id") Long id) {
        return ResultUtils.success(pollItemService.get(id));
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
            return ResultUtils.failure("参数错误");
        }
        return ResultUtils.success(pollItemService.getListByNetwork(network, page - 1, count));
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
    public Result addPollItem(Long againstVotes, String creator, String description, String descriptionEn, Long endTime, Long forVotes, String link, String title, String titleEn, String typeArgs1, Integer status, String network) throws UnsupportedEncodingException {
        boolean result = pollItemService.add(againstVotes, creator, URLDecoder.decode(description, "UTF-8"), URLDecoder.decode(descriptionEn, "UTF-8"), endTime, forVotes, link, title, titleEn, typeArgs1, status, network);
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
    public Result modifPollItem(Long id, Long againstVotes, String creator, String description, String descriptionEn, Long endTime, Long forVotes, String link, String title, String titleEn, String typeArgs1, Integer status, String network) throws UnsupportedEncodingException {
        boolean result = pollItemService.modif(id, againstVotes, creator, URLDecoder.decode(description, "UTF-8"), URLDecoder.decode(descriptionEn, "UTF-8"), endTime, forVotes, link, title, titleEn, typeArgs1, status, network);
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
            @ApiImplicitParam(name = "proposer", value = "proposer", required = true, dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "page", value = "页码，默认第1页", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "count", value = "条数，默认为20条", dataType = "Integer", dataTypeClass = Integer.class)
    })
    @ApiResponse(code = 200, message = "SUCCESS", response = Result.class)
    @GetMapping("/votes/{network}/{proposalId}/{proposer}/page/{page}")
    public Result<PageResult> delPollItem(@PathVariable("network") String network,
                                          @PathVariable("proposalId") Long proposalId,
                                          @PathVariable("proposer") String proposer,
                                          @PathVariable("page") int page,
                                          @RequestParam(value = "count", required = false, defaultValue = "20") int count) {
        String url = "http://apitest.stcscan.io:8500/v1/transaction/getEventByProposalIdAndProposer/" + network + "/" + proposalId + "/" + proposer + "/page/" + page + "?count=" + count;
        logger.info("HttpUtils.get_request：" + url);
        String resultStr = restTemplate.getForObject(url, String.class);
        logger.info("HttpUtils.get_response：" + resultStr);
        JSONObject result = JSONObject.parseObject(resultStr);
        PageResult pageResult = new PageResult<>();
        pageResult.setCurrentPage(page);
        pageResult.setTotalPage(0);
        pageResult.setTotalElements(result.getIntValue("total"));
        pageResult.setList(result.getJSONArray("contents"));
        return ResultUtils.success(pageResult);
    }
}
