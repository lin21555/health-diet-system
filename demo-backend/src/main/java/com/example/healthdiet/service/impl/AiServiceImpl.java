package com.example.healthdiet.service.impl;

import com.example.healthdiet.entity.AiQaRecord;
import com.example.healthdiet.service.AiQaRecordService;
import com.example.healthdiet.service.AiService;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    private final AiQaRecordService aiQaRecordService;

    public AiServiceImpl(AiQaRecordService aiQaRecordService) {
        this.aiQaRecordService = aiQaRecordService;
    }

    @Override
    public AiQaRecord ask(Long userId, String question) {
        String safeQuestion = question == null ? "" : question;
        String answer;
        if (safeQuestion.contains("减脂")) {
            answer = "减脂饮食建议控制总热量，优先选择高蛋白、低油、富含膳食纤维的食物，例如鸡胸肉、鱼肉、蔬菜、粗粮等。";
        } else if (safeQuestion.contains("早餐")) {
            answer = "早餐可搭配主食、优质蛋白和蔬果，例如燕麦牛奶、鸡蛋、全麦面包和水果。";
        } else if (safeQuestion.contains("增肌")) {
            answer = "增肌人群可适当增加优质蛋白和复合碳水摄入，例如牛肉、鸡蛋、鱼肉、米饭、意面等。";
        } else if (safeQuestion.contains("控糖")) {
            answer = "控糖饮食可减少高糖食物，优先选择粗粮、蔬菜和优质蛋白。相关建议仅作为日常饮食参考。";
        } else {
            answer = "建议保持食物多样、荤素搭配、粗细搭配，并根据个人饮食目标控制总热量。";
        }
        answer = answer + " 本回答仅作为健康饮食参考，不作为医疗诊断或治疗依据。";

        AiQaRecord record = new AiQaRecord();
        record.setUserId(userId);
        record.setQuestion(safeQuestion);
        record.setAnswer(answer);
        aiQaRecordService.save(record);
        return record;
    }
}
