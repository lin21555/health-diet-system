package com.example.healthdiet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.healthdiet.entity.Food;

import java.util.List;

public interface FoodService extends IService<Food> {
    List<Food> listByCondition(String keyword, String category);
}
