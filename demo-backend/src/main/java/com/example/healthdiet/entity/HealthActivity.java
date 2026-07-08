package com.example.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("health_activity")
public class HealthActivity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String image;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime signupStartTime;
    private LocalDateTime signupEndTime;
    private String status;
    private String auditStatus;
    private Integer capacity;
    private Integer signupCount;
    private String description;
    private String content;
    private LocalDateTime createTime;
}
