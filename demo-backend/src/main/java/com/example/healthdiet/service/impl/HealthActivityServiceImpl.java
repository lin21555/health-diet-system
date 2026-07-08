package com.example.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.healthdiet.entity.ActivitySignup;
import com.example.healthdiet.entity.HealthActivity;
import com.example.healthdiet.mapper.HealthActivityMapper;
import com.example.healthdiet.service.ActivitySignupService;
import com.example.healthdiet.service.HealthActivityService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthActivityServiceImpl extends ServiceImpl<HealthActivityMapper, HealthActivity> implements HealthActivityService {

    private final ActivitySignupService activitySignupService;

    public HealthActivityServiceImpl(ActivitySignupService activitySignupService) {
        this.activitySignupService = activitySignupService;
    }

    @Override
    public List<HealthActivity> listByCondition(String keyword, String status) {
        LambdaQueryWrapper<HealthActivity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(HealthActivity::getTitle, keyword)
                    .or()
                    .like(HealthActivity::getDescription, keyword)
                    .or()
                    .like(HealthActivity::getLocation, keyword));
        }

        if (StringUtils.hasText(status) && !"全部".equals(status)) {
            wrapper.eq(HealthActivity::getStatus, status);
        }

        wrapper.orderByDesc(HealthActivity::getId);
        return list(wrapper);
    }

    @Override
    public String signup(Long activityId, Long userId) {
        HealthActivity activity = getById(activityId);
        if (activity == null) {
            return "活动不存在或已被删除";
        }
        if (!"进行中".equals(activity.getStatus())) {
            return "当前活动不在报名进行状态";
        }

        LambdaQueryWrapper<ActivitySignup> existsWrapper = new LambdaQueryWrapper<>();
        existsWrapper.eq(ActivitySignup::getActivityId, activityId)
                .eq(ActivitySignup::getUserId, userId);
        ActivitySignup exists = activitySignupService.getOne(existsWrapper, false);
        if (exists != null) {
            return "你已报名该活动，无需重复报名";
        }

        Integer count = activity.getSignupCount() == null ? 0 : activity.getSignupCount();
        Integer capacity = activity.getCapacity() == null ? 0 : activity.getCapacity();
        if (capacity > 0 && count >= capacity) {
            return "活动名额已满";
        }

        ActivitySignup signup = new ActivitySignup();
        signup.setActivityId(activityId);
        signup.setUserId(userId);
        signup.setStatus("已报名");
        signup.setCreateTime(LocalDateTime.now());
        activitySignupService.save(signup);

        activity.setSignupCount(count + 1);
        updateById(activity);
        return "报名成功";
    }

    @Override
    public List<HealthActivity> joinedList(Long userId) {
        LambdaQueryWrapper<ActivitySignup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivitySignup::getUserId, userId)
                .orderByDesc(ActivitySignup::getId);

        List<ActivitySignup> signups = activitySignupService.list(wrapper);
        if (signups == null || signups.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> ids = signups.stream()
                .map(ActivitySignup::getActivityId)
                .distinct()
                .collect(Collectors.toList());

        return listByIds(ids);
    }
}
