package com.example.healthdiet.service;

import com.example.healthdiet.entity.AiQaRecord;

public interface AiService {
    AiQaRecord ask(Long userId, String question);
}
