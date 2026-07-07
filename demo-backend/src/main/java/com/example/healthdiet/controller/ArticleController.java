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

    /**
     * 资讯详情接口。
     * 同时兼容 /api/article/{id} 和 /api/article/detail/{id}，避免前端路径与后端接口不一致导致详情页一直加载。
     */
    @GetMapping({"/{id}", "/detail/{id}"})
    public Result<Article> detail(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.error("资讯不存在");
        }

        // 浏览量统计失败不应该影响详情展示，所以这里做容错处理。
        try {
            Integer count = article.getViewCount() == null ? 0 : article.getViewCount();
            article.setViewCount(count + 1);
            articleService.updateById(article);
        } catch (Exception ignored) {
            // ignore
        }

        return Result.success(article);
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
