package com.example.healthdiet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.healthdiet.entity.HealthActivity;

import java.util.List;

public interface HealthActivityService extends IService<HealthActivity> {

    List<HealthActivity> listByCondition(String keyword, String status);

    String signup(Long activityId, Long userId);

    List<HealthActivity> joinedList(Long userId);
}
