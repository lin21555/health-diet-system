package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.entity.Article;
import com.example.healthdiet.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/list")
    public Result<List<Article>> list() {
        return Result.success(articleService.lambdaQuery().orderByDesc(Article::getCreateTime).list());
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Article article) {
        return Result.success(articleService.save(article));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Article article) {
        return Result.success(articleService.updateById(article));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(articleService.removeById(id));
    }
}
