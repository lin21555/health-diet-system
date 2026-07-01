package com.example.healthdiet.dto;

import lombok.Data;

@Data
public class AiAskRequest {
    private Long userId;
    private String question;
}
