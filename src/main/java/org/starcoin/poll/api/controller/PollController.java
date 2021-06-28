package org.starcoin.poll.api.controller;

import org.springframework.web.bind.annotation.*;
import org.starcoin.poll.api.service.PollItemService;
import org.starcoin.poll.api.vo.Result;
import org.starcoin.poll.api.vo.ResultUtils;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("v1/poll")
public class PollController {

    @Resource
    private PollItemService pollItemService;

    @GetMapping("/get/{id}")
    public Result getPollItem(@PathVariable("id") Long id) throws IOException {
        return ResultUtils.success(pollItemService.get(id));
    }

    @GetMapping("/list/page/{page}")
    public Result getList(@PathVariable("page") int page, @RequestParam(value = "count", required = false, defaultValue = "20") int count) throws IOException {
        if (page - 1 < 0) {
            return ResultUtils.failure("参数错误");
        }
        return ResultUtils.success(pollItemService.getList(page - 1, count));
    }

    @PostMapping("/add")
    public Result addPollItem(Long id, Integer againstVotes, String creator, String description, String descriptionEn, Long endTime, Integer forVotes, String link, String title, String titleEn, String typeArgs1, String status) throws IOException {
        boolean result = pollItemService.add(id, againstVotes, creator, description, descriptionEn, endTime, forVotes, link, title, titleEn, typeArgs1, status);
        if (result) {
            return ResultUtils.success();
        }
        return ResultUtils.failure();
    }

    @PostMapping("/modif")
    public Result modifPollItem(Long id, Integer againstVotes, String creator, String description, String descriptionEn, Long endTime, Integer forVotes, String link, String title, String titleEn, String typeArgs1, String status) throws IOException {
        boolean result = pollItemService.modif(id, againstVotes, creator, description, descriptionEn, endTime, forVotes, link, title, titleEn, typeArgs1, status);
        if (result) {
            return ResultUtils.success();
        }
        return ResultUtils.failure();
    }

    @GetMapping("/del/{id}")
    public Result delPollItem(@PathVariable("id") Long id) throws IOException {
        pollItemService.del(id);
        return ResultUtils.success();
    }
}
