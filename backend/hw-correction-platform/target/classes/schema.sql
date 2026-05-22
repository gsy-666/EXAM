CREATE DATABASE IF NOT EXISTS `hw_correction` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `hw_correction`;

-- 用户表
CREATE TABLE IF NOT EXISTS `t_user` (
    `user_id` VARCHAR(32) PRIMARY KEY COMMENT '用户唯一标识',
    `username` VARCHAR(50) NOT NULL COMMENT '登录名',
    `password` VARCHAR(64) NOT NULL COMMENT '密码',
    `role` TINYINT NOT NULL COMMENT '角色 1-学生 2-教师 3-管理员',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `college` VARCHAR(100) COMMENT '学院',
    `major` VARCHAR(100) COMMENT '专业',
    `class_name` VARCHAR(50) COMMENT '班级',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 作业表
CREATE TABLE IF NOT EXISTS `t_homework` (
    `homework_id` VARCHAR(32) PRIMARY KEY COMMENT '作业唯一标识',
    `course_id` VARCHAR(32) NOT NULL COMMENT '课程ID',
    `teacher_id` VARCHAR(32) NOT NULL COMMENT '发布教师ID',
    `title` VARCHAR(255) NOT NULL COMMENT '作业标题',
    `content` TEXT COMMENT '作业内容描述',
    `deadline` DATETIME NOT NULL COMMENT '截止时间',
    `total_score` DECIMAL(5,2) DEFAULT '100.00' COMMENT '总分',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业信息表';

-- 题目表
CREATE TABLE IF NOT EXISTS `t_question` (
    `question_id` VARCHAR(32) PRIMARY KEY COMMENT '题目唯一标识',
    `homework_id` VARCHAR(32) NOT NULL COMMENT '关联作业ID',
    `question_type` TINYINT NOT NULL COMMENT '题型：1单选 2多选 3判断 4填空 5简答 6论述 7其他',
    `content` TEXT NOT NULL COMMENT '题干内容',
    `standard_answer` TEXT COMMENT '标准答案',
    `score` DECIMAL(5,2) NOT NULL COMMENT '本题分值',
    `rule_config` TEXT COMMENT '智能批改规则配置(JSON)',
    `knowledge_tag` VARCHAR(255) COMMENT '知识点标签(逗号分隔)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目信息表';

-- 批改记录表
CREATE TABLE IF NOT EXISTS `t_correction` (
    `correction_id` VARCHAR(32) PRIMARY KEY COMMENT '批改记录唯一标识',
    `homework_id` VARCHAR(32) NOT NULL COMMENT '作业ID',
    `student_id` VARCHAR(32) NOT NULL COMMENT '学生ID',
    `question_id` VARCHAR(32) NOT NULL COMMENT '题目ID',
    `student_answer` TEXT COMMENT '学生提交答案',
    `score` DECIMAL(5,2) COMMENT '批改得分',
    `comment` TEXT COMMENT '批改评语',
    `right_flag` TINYINT COMMENT '正确标志：1-全对 2-半对 0-错',
    `correction_type` TINYINT DEFAULT '1' COMMENT '1-智能批改 2-人工批改',
    `correction_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '批改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批改记录表';

-- 学情数据表
CREATE TABLE IF NOT EXISTS `t_learning_data` (
    `data_id` VARCHAR(32) PRIMARY KEY,
    `user_id` VARCHAR(32) NOT NULL,
    `course_id` VARCHAR(32) NOT NULL,
    `knowledge_tag` VARCHAR(255) NOT NULL,
    `correct_rate` DECIMAL(5,2) DEFAULT '0.00',
    `total_count` INT DEFAULT '0',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学情数据表';
