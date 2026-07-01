package com.example.healthdiet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.healthdiet.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
    User register(User user);
}
