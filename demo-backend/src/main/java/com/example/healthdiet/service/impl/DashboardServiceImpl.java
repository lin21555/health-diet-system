package com.example.healthdiet.service.impl;

import com.example.healthdiet.mapper.*;
import com.example.healthdiet.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;
    private final RecipeMapper recipeMapper;
    private final ArticleMapper articleMapper;
    private final ForumPostMapper forumPostMapper;
    private final MessageMapper messageMapper;
    private final AiQaRecordMapper aiQaRecordMapper;
    private final RecommendRecordMapper recommendRecordMapper;

    public DashboardServiceImpl(UserMapper userMapper,
                                RecipeMapper recipeMapper,
                                ArticleMapper articleMapper,
                                ForumPostMapper forumPostMapper,
                                MessageMapper messageMapper,
                                AiQaRecordMapper aiQaRecordMapper,
                                RecommendRecordMapper recommendRecordMapper) {
        this.userMapper = userMapper;
        this.recipeMapper = recipeMapper;
        this.articleMapper = articleMapper;
        this.forumPostMapper = forumPostMapper;
        this.messageMapper = messageMapper;
        this.aiQaRecordMapper = aiQaRecordMapper;
        this.recommendRecordMapper = recommendRecordMapper;
    }

    @Override
    public Map<String, Object> summary() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userCount", userMapper.selectCount(null));
        map.put("recipeCount", recipeMapper.selectCount(null));
        map.put("articleCount", articleMapper.selectCount(null));
        map.put("postCount", forumPostMapper.selectCount(null));
        map.put("messageCount", messageMapper.selectCount(null));
        map.put("aiCount", aiQaRecordMapper.selectCount(null));
        map.put("recommendCount", recommendRecordMapper.selectCount(null));
        return map;
    }
}
