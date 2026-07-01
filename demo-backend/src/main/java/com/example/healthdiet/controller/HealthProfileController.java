package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.entity.HealthProfile;
import com.example.healthdiet.service.HealthProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
public class HealthProfileController {

    private final HealthProfileService healthProfileService;

    public HealthProfileController(HealthProfileService healthProfileService) {
        this.healthProfileService = healthProfileService;
    }

    @GetMapping("/profile/{userId}")
    public Result<HealthProfile> getProfile(@PathVariable Long userId) {
        return Result.success(healthProfileService.lambdaQuery().eq(HealthProfile::getUserId, userId).one());
    }

    @PostMapping("/profile")
    public Result<HealthProfile> saveProfile(@RequestBody HealthProfile profile) {
        return Result.success(healthProfileService.saveOrUpdateByUser(profile));
    }
}
