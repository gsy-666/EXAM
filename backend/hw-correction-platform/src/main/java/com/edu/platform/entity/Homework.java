package com.edu.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_homework")
public class Homework {
    @TableId(type = IdType.ASSIGN_ID)
    private String homeworkId;
    private String courseId;
    private String teacherId;
    private String title;
    private String content;
    private LocalDateTime deadline;
    private BigDecimal totalScore;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
