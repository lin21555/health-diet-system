package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.dto.RecommendRequest;
import com.example.healthdiet.service.RecommendService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @PostMapping("/generate")
    public Result<Map<String, Object>> generate(@RequestBody RecommendRequest request) {
        return Result.success(recommendService.generate(request));
    }


    @GetMapping("/history/{userId}")
    public Result<List<Map<String, Object>>> history(@PathVariable Long userId) {
        return Result.success(recommendService.history(userId));
    }
}
