package com.example.healthdiet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.healthdiet.entity.HealthProfile;

public interface HealthProfileService extends IService<HealthProfile> {
    HealthProfile saveOrUpdateByUser(HealthProfile profile);
}
