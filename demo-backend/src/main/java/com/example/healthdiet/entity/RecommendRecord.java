package com.example.healthdiet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("recommend_record")
public class RecommendRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long breakfastId;
    private Long lunchId;
    private Long dinnerId;
    private Long snackId;
    private String recommendReason;
    private LocalDateTime createTime;
}
