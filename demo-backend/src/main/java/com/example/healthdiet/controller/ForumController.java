package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.entity.ForumPost;
import com.example.healthdiet.service.ForumPostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    private final ForumPostService forumPostService;

    public ForumController(ForumPostService forumPostService) {
        this.forumPostService = forumPostService;
    }

    @GetMapping("/list")
    public Result<List<ForumPost>> list() {
        return Result.success(forumPostService.lambdaQuery().orderByDesc(ForumPost::getCreateTime).list());
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody ForumPost post) {
        if (post.getViewCount() == null) {
            post.setViewCount(0);
        }
        if (post.getCommentCount() == null) {
            post.setCommentCount(0);
        }
        return Result.success(forumPostService.save(post));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(forumPostService.removeById(id));
    }
}
