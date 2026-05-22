package com.edu.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.platform.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
}