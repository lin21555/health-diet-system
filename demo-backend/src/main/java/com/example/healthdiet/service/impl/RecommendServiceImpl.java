package com.example.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.healthdiet.dto.RecommendRequest;
import com.example.healthdiet.entity.HealthProfile;
import com.example.healthdiet.entity.Recipe;
import com.example.healthdiet.entity.RecommendRecord;
import com.example.healthdiet.mapper.RecommendRecordMapper;
import com.example.healthdiet.service.HealthProfileService;
import com.example.healthdiet.service.RecipeService;
import com.example.healthdiet.service.RecommendService;
import com.example.healthdiet.util.BmiUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendServiceImpl implements RecommendService {

    private final RecipeService recipeService;
    private final HealthProfileService healthProfileService;
    private final RecommendRecordMapper recommendRecordMapper;

    public RecommendServiceImpl(RecipeService recipeService,
                                HealthProfileService healthProfileService,
                                RecommendRecordMapper recommendRecordMapper) {
        this.recipeService = recipeService;
        this.healthProfileService = healthProfileService;
        this.recommendRecordMapper = recommendRecordMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> generate(RecommendRequest request) {
        BigDecimal bmi = BmiUtil.calculate(request.getHeight(), request.getWeight());
        String level = BmiUtil.level(bmi);

        if (request.getUserId() != null) {
            HealthProfile profile = new HealthProfile();
            profile.setUserId(request.getUserId());
            profile.setAge(request.getAge());
            profile.setGender(request.getGender());
            profile.setHeight(request.getHeight());
            profile.setWeight(request.getWeight());
            profile.setActivityLevel(request.getActivityLevel());
            profile.setDietGoal(request.getDietGoal());
            profile.setTastePreference(request.getTastePreference());
            profile.setAllergyFoods(request.getAllergyFoods());
            healthProfileService.saveOrUpdateByUser(profile);
        }

        Recipe breakfast = firstByCategory("早餐", request.getDietGoal(), request.getAllergyFoods());
        Recipe lunch = firstByCategory(request.getDietGoal(), null, request.getAllergyFoods());
        Recipe dinner = firstByCategory("晚餐", request.getDietGoal(), request.getAllergyFoods());

        String reason = "系统根据用户身高、体重、BMI状态、饮食目标、饮食偏好与食谱营养标签进行规则匹配。推荐结果仅作为日常健康饮食参考，不作为医疗诊断或治疗依据。";

        RecommendRecord record = new RecommendRecord();
        record.setUserId(request.getUserId());
        record.setBreakfastId(breakfast == null ? null : breakfast.getId());
        record.setLunchId(lunch == null ? null : lunch.getId());
        record.setDinnerId(dinner == null ? null : dinner.getId());
        record.setRecommendReason(reason);
        if (request.getUserId() != null) {
            recommendRecordMapper.insert(record);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("bmi", bmi);
        result.put("bmiLevel", level);
        result.put("breakfast", breakfast);
        result.put("lunch", lunch);
        result.put("dinner", dinner);
        result.put("reason", reason);
        return result;
    }

    private Recipe firstByCategory(String category, String goal, String allergyFoods) {
        LambdaQueryWrapper<Recipe> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(category)) {
            wrapper.and(w -> w.eq(Recipe::getCategory, category)
                    .or().like(Recipe::getTags, category)
                    .or().like(Recipe::getSuitablePeople, category));
        }
        if (StringUtils.hasText(goal)) {
            wrapper.and(w -> w.like(Recipe::getCategory, goal)
                    .or().like(Recipe::getTags, goal)
                    .or().like(Recipe::getSuitablePeople, goal));
        }
        if (StringUtils.hasText(allergyFoods) && !"无".equals(allergyFoods)) {
            for (String allergy : allergyFoods.split("[,，、 ]")) {
                if (StringUtils.hasText(allergy)) {
                    wrapper.notLike(Recipe::getIngredients, allergy.trim());
                }
            }
        }
        wrapper.orderByAsc(Recipe::getCalorie).last("LIMIT 1");
        Recipe recipe = recipeService.getOne(wrapper, false);
        if (recipe != null) {
            return recipe;
        }
        List<Recipe> all = recipeService.list(new LambdaQueryWrapper<Recipe>().orderByAsc(Recipe::getCalorie).last("LIMIT 1"));
        return all.isEmpty() ? null : all.get(0);
    }


    @Override
    public List<Map<String, Object>> history(Long userId) {
        List<RecommendRecord> records = recommendRecordMapper.selectList(new LambdaQueryWrapper<RecommendRecord>()
                .eq(RecommendRecord::getUserId, userId)
                .orderByDesc(RecommendRecord::getCreateTime));
        return records.stream().map(record -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", record.getId());
            item.put("createTime", record.getCreateTime());
            item.put("recommendReason", record.getRecommendReason());
            item.put("breakfast", record.getBreakfastId() == null ? null : recipeService.getById(record.getBreakfastId()));
            item.put("lunch", record.getLunchId() == null ? null : recipeService.getById(record.getLunchId()));
            item.put("dinner", record.getDinnerId() == null ? null : recipeService.getById(record.getDinnerId()));
            return item;
        }).toList();
    }
}
