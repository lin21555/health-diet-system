package com.example.healthdiet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.healthdiet.entity.HealthProfile;
import com.example.healthdiet.mapper.HealthProfileMapper;
import com.example.healthdiet.service.HealthProfileService;
import com.example.healthdiet.util.BmiUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthProfileServiceImpl extends ServiceImpl<HealthProfileMapper, HealthProfile> implements HealthProfileService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HealthProfile saveOrUpdateByUser(HealthProfile profile) {
        profile.setBmi(BmiUtil.calculate(profile.getHeight(), profile.getWeight()));
        HealthProfile old = lambdaQuery().eq(HealthProfile::getUserId, profile.getUserId()).one();
        if (old == null) {
            save(profile);
        } else {
            profile.setId(old.getId());
            updateById(profile);
        }
        return profile;
    }
}
