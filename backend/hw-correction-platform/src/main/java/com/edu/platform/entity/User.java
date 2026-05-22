package com.edu.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private String userId;
    private String username;
    private String password;
    private Integer role; // 1-学生 2-教师 3-管理员
    private String name;
    private String college;
    private String major;
    private String className;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}