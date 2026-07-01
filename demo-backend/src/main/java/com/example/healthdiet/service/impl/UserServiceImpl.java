package com.example.healthdiet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.healthdiet.common.Constants;
import com.example.healthdiet.entity.User;
import com.example.healthdiet.mapper.UserMapper;
import com.example.healthdiet.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        return lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .eq(User::getStatus, 1)
                .one();
    }

    @Override
    public User register(User user) {
        Long count = lambdaQuery().eq(User::getUsername, user.getUsername()).count();
        if (count != null && count > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (!StringUtils.hasText(user.getRole())) {
            user.setRole(Constants.ROLE_USER);
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        save(user);
        return user;
    }
}
