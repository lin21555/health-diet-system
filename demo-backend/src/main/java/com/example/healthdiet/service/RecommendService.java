package com.example.healthdiet.service;

import com.example.healthdiet.dto.RecommendRequest;

import java.util.List;
import java.util.Map;

public interface RecommendService {
    Map<String, Object> generate(RecommendRequest request);
    List<Map<String, Object>> history(Long userId);
}
