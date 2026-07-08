package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.entity.HealthActivity;
import com.example.healthdiet.service.HealthActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class HealthActivityController {

    private final HealthActivityService healthActivityService;

    public HealthActivityController(HealthActivityService healthActivityService) {
        this.healthActivityService = healthActivityService;
    }

    /**
     * 活动列表
     */
    @GetMapping("/list")
    public Result<List<HealthActivity>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                             @RequestParam(value = "status", required = false) String status) {
        return Result.success(healthActivityService.listByCondition(keyword, status));
    }

    /**
     * 活动详情
     */
    @GetMapping("/{id}")
    public Result<HealthActivity> detail(@PathVariable("id") Long id) {
        HealthActivity activity = healthActivityService.getById(id);
        if (activity == null) {
            return Result.error("活动不存在或已被删除");
        }
        return Result.success(activity);
    }

    /**
     * 报名活动
     */
    @PostMapping("/signup/{id}")
    public Result<String> signup(@PathVariable("id") Long id,
                                 @RequestParam(value = "userId") Long userId) {
        String message = healthActivityService.signup(id, userId);
        if ("报名成功".equals(message)) {
            return Result.success(message);
        }
        return Result.error(message);
    }

    /**
     * 我的报名活动
     */
    @GetMapping("/joined")
    public Result<List<HealthActivity>> joined(@RequestParam(value = "userId") Long userId) {
        return Result.success(healthActivityService.joinedList(userId));
    }

    /**
     * 新增活动
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody HealthActivity activity) {
        return Result.success(healthActivityService.save(activity));
    }

    /**
     * 修改活动
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody HealthActivity activity) {
        return Result.success(healthActivityService.updateById(activity));
    }

    /**
     * 删除活动
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.success(healthActivityService.removeById(id));
    }
}
