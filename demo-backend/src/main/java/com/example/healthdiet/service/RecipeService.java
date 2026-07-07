package com.example.healthdiet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.healthdiet.entity.Recipe;

import java.util.List;

public interface RecipeService extends IService<Recipe> {
    List<Recipe> listByCondition(String keyword, String category);
    void favorite(Long userId, Long recipeId);
    List<Recipe> favoriteList(Long userId);
}
