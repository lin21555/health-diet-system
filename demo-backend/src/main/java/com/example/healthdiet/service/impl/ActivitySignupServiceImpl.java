package com.example.healthdiet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.healthdiet.entity.ActivitySignup;
import com.example.healthdiet.mapper.ActivitySignupMapper;
import com.example.healthdiet.service.ActivitySignupService;
import org.springframework.stereotype.Service;

@Service
public class ActivitySignupServiceImpl extends ServiceImpl<ActivitySignupMapper, ActivitySignup> implements ActivitySignupService {
}
