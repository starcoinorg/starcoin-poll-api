package org.starcoin.poll.api.controller;

import org.springframework.web.bind.annotation.*;
import org.starcoin.poll.api.service.PollItemService;
import org.starcoin.poll.api.vo.Result;
import org.starcoin.poll.api.vo.ResultUtils;

import javax.annotation.Resource;

@RestController
@RequestMapping("v1/poll")
public class PollController {

    @Resource
    private PollItemService pollItemService;

    @GetMapping("/get/{id}")
    public Result getPollItem(@PathVariable("id") Long id) {
        return ResultUtils.success(pollItemService.get(id));
    }

    @GetMapping("/list/{network}/page/{page}")
    public Result getList(@PathVariable("network") String network, @PathVariable("page") int page, @RequestParam(value = "count", required = false, defaultValue = "20") int count) {
        if (page - 1 < 0) {
            return ResultUtils.failure("参数错误");
        }
        return ResultUtils.success(pollItemService.getListByNetwork(network, page - 1, count));
    }

    @PostMapping("/add")
    public Result addPollItem(Integer againstVotes, String creator, String description, String descriptionEn, Long endTime, Integer forVotes, String link, String title, String titleEn, String typeArgs1, String status, String network) {
        boolean result = pollItemService.add(againstVotes, creator, description, descriptionEn, endTime, forVotes, link, title, titleEn, typeArgs1, status, network);
        if (result) {
            return ResultUtils.success();
        }
        return ResultUtils.failure();
    }

    @PostMapping("/modif")
    public Result modifPollItem(Long id, Integer againstVotes, String creator, String description, String descriptionEn, Long endTime, Integer forVotes, String link, String title, String titleEn, String typeArgs1, String status, String network) {
        boolean result = pollItemService.modif(id, againstVotes, creator, description, descriptionEn, endTime, forVotes, link, title, titleEn, typeArgs1, status, network);
        if (result) {
            return ResultUtils.success();
        }
        return ResultUtils.failure();
    }

    @GetMapping("/del/{id}")
    public Result delPollItem(@PathVariable("id") Long id) {
        pollItemService.del(id);
        return ResultUtils.success();
    }
}
