package com.example.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.healthdiet.entity.Food;
import com.example.healthdiet.mapper.FoodMapper;
import com.example.healthdiet.service.FoodService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    @Override
    public List<Food> listByCondition(String keyword, String category) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Food::getName, keyword)
                    .or()
                    .like(Food::getDescription, keyword));
        }

        if (StringUtils.hasText(category) && !"全部".equals(category)) {
            wrapper.eq(Food::getCategory, category);
        }

        wrapper.orderByAsc(Food::getId);
        return list(wrapper);
    }
}
