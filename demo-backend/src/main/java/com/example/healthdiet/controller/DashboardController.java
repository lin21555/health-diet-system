package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/api/dashboard/summary")
    public Result<Map<String, Object>> summary() {
        return Result.success(dashboardService.summary());
    }
}
