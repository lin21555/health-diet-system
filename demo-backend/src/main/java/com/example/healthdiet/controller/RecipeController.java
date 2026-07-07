package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.entity.Recipe;
import com.example.healthdiet.service.RecipeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/list")
    public Result<List<Recipe>> list(@RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) String category) {
        return Result.success(recipeService.listByCondition(keyword, category));
    }

    @GetMapping("/{id}")
    public Result<Recipe> detail(@PathVariable Long id) {
        Recipe recipe = recipeService.getById(id);
        if (recipe == null) {
            return Result.error("食谱不存在");
        }
        recipe.setViewCount((recipe.getViewCount() == null ? 0 : recipe.getViewCount()) + 1);
        recipeService.updateById(recipe);
        return Result.success(recipe);
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Recipe recipe) {
        return Result.success(recipeService.save(recipe));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Recipe recipe) {
        return Result.success(recipeService.updateById(recipe));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(recipeService.removeById(id));
    }

    @PostMapping("/favorite/{id}")
    public Result<String> favorite(@PathVariable Long id,
                                   @RequestParam(defaultValue = "2") Long userId) {
        recipeService.favorite(userId, id);
        return Result.success("收藏成功");
    }


    @GetMapping("/favorite/list")
    public Result<List<Recipe>> favoriteList(@RequestParam(defaultValue = "2") Long userId) {
        return Result.success(recipeService.favoriteList(userId));
    }
}
