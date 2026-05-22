package com.edu.platform.controller;

import com.edu.platform.common.Result;
import com.edu.platform.entity.User;
import com.edu.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "统一用户认证与管理", description = "供学生、教师、管理员通用的登录、查询接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户登录", description = "根据用户名和密码验证登录")
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        User user = userService.login(username, password);
        if (user != null) {
            // 真实项目中这里应该生成并返回 JWT token
            // 此处为简化安全逻辑，直接返回用户实体（前端可以存在本地存储中）
            user.setPassword(null); // 消除密码防止泄露
            return Result.success("登录成功", user);
        }
        return Result.error("用户名或密码错误");
    }

    @Operation(summary = "获取当前用户信息", description = "通过userId获取用户详情")
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestParam String userId) {
        User user = userService.getById(userId);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }
}