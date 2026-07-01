package com.example.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("health_profile")
public class HealthProfile {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer age;
    private String gender;
    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal bmi;
    private String activityLevel;
    private String dietGoal;
    private String tastePreference;
    private String allergyFoods;
    private String avoidFoods;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
