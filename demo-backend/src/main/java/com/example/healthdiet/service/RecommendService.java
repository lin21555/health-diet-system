package com.example.healthdiet.service;

import com.example.healthdiet.dto.RecommendRequest;

import java.util.Map;

public interface RecommendService {
    Map<String, Object> generate(RecommendRequest request);
}
