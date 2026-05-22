-- 种子数据：测试用户
-- 先执行 schema.sql 建表后再执行此文件

USE `hw_correction`;

-- 教师账号
INSERT INTO `t_user` (`user_id`, `username`, `password`, `role`, `name`, `college`, `major`, `class_name`) VALUES
('1', 'teacher1', '123456', 2, '张教授', '计算机学院', '计算机科学与技术', '计科2101');

-- 学生账号
INSERT INTO `t_user` (`user_id`, `username`, `password`, `role`, `name`, `college`, `major`, `class_name`) VALUES
('2', 'student1', '123456', 1, '李明', '计算机学院', '计算机科学与技术', '计科2101'),
('3', 'student2', '123456', 1, '王芳', '计算机学院', '软件工程', '软工2102');
