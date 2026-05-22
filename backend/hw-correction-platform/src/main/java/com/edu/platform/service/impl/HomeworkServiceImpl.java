package com.edu.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.platform.entity.Homework;
import com.edu.platform.mapper.HomeworkMapper;
import com.edu.platform.service.HomeworkService;
import org.springframework.stereotype.Service;

@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements HomeworkService {
}