package com.example.healthdiet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecommendRequest {
    private Long userId;
    private Integer age;
    private String gender;
    private BigDecimal height;
    private BigDecimal weight;
    private String activityLevel;
    private String dietGoal;
    private String tastePreference;
    private String allergyFoods;
}
