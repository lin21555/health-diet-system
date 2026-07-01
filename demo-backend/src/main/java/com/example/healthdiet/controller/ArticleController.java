package com.example.healthdiet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    /**
     * 查询资讯列表
     */
    @GetMapping("/list")
    public Result<List<Article>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                      @RequestParam(value = "category", required = false) String category) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(Article::getTitle, keyword)
                    .or()
                    .like(Article::getContent, keyword));
        }

        if (category != null && !category.trim().isEmpty() && !"全部".equals(category)) {
            wrapper.eq(Article::getCategory, category);
        }

        wrapper.orderByDesc(Article::getId);

        return Result.success(articleService.list(wrapper));
    }

    /**
     * 资讯详情接口：
     * GET /api/article/3
     */
    @GetMapping("/{id}")
    public Result<Article> detail(@PathVariable("id") Long id) {
        return getArticleDetail(id);
    }

    /**
     * 资讯详情兼容接口：
     * GET /api/article/detail/3
     */
    @GetMapping("/detail/{id}")
    public Result<Article> detailByPath(@PathVariable("id") Long id) {
        return getArticleDetail(id);
    }

    private Result<Article> getArticleDetail(Long id) {
        Article article = articleService.getById(id);

        if (article == null) {
            return Result.error("资讯不存在或已被删除");
        }

        return Result.success(article);
    }

    /**
     * 新增资讯
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody Article article) {
        return Result.success(articleService.save(article));
    }

    /**
     * 修改资讯
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody Article article) {
        return Result.success(articleService.updateById(article));
    }

    /**
     * 删除资讯
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.success(articleService.removeById(id));
    }
}