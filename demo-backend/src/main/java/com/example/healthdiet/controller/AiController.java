package com.example.healthdiet.controller;

import com.example.healthdiet.common.Result;
import com.example.healthdiet.dto.AiAskRequest;
import com.example.healthdiet.entity.AiQaRecord;
import com.example.healthdiet.service.AiQaRecordService;
import com.example.healthdiet.service.AiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;
    private final AiQaRecordService aiQaRecordService;

    public AiController(AiService aiService, AiQaRecordService aiQaRecordService) {
        this.aiService = aiService;
        this.aiQaRecordService = aiQaRecordService;
    }

    @PostMapping("/ask")
    public Result<AiQaRecord> ask(@RequestBody AiAskRequest request) {
        return Result.success(aiService.ask(request.getUserId(), request.getQuestion()));
    }

    @GetMapping("/history")
    public Result<List<AiQaRecord>> history() {
        return Result.success(aiQaRecordService.lambdaQuery().orderByDesc(AiQaRecord::getCreateTime).list());
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(aiQaRecordService.removeById(id));
    }
}
