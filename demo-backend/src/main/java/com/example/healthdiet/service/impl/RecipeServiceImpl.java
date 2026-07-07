package com.example.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.healthdiet.entity.Favorite;
import com.example.healthdiet.entity.Recipe;
import com.example.healthdiet.mapper.FavoriteMapper;
import com.example.healthdiet.mapper.RecipeMapper;
import com.example.healthdiet.service.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {

    private final FavoriteMapper favoriteMapper;

    public RecipeServiceImpl(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    public List<Recipe> listByCondition(String keyword, String category) {
        LambdaQueryWrapper<Recipe> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Recipe::getName, keyword)
                    .or().like(Recipe::getTags, keyword)
                    .or().like(Recipe::getSuitablePeople, keyword));
        }
        if (StringUtils.hasText(category) && !"全部".equals(category)) {
            wrapper.and(w -> w.eq(Recipe::getCategory, category)
                    .or().like(Recipe::getTags, category)
                    .or().like(Recipe::getSuitablePeople, category));
        }
        wrapper.orderByDesc(Recipe::getCollectCount, Recipe::getViewCount);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void favorite(Long userId, Long recipeId) {
        Long count = favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getRecipeId, recipeId));
        if (count == null || count == 0) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setRecipeId(recipeId);
            favoriteMapper.insert(favorite);
            Recipe recipe = getById(recipeId);
            if (recipe != null) {
                recipe.setCollectCount((recipe.getCollectCount() == null ? 0 : recipe.getCollectCount()) + 1);
                updateById(recipe);
            }
        }
    }


    @Override
    public List<Recipe> favoriteList(Long userId) {
        List<Favorite> favorites = favoriteMapper.selectList(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime));
        if (favorites == null || favorites.isEmpty()) {
            return List.of();
        }
        List<Long> recipeIds = favorites.stream().map(Favorite::getRecipeId).toList();
        return listByIds(recipeIds);
    }
}
