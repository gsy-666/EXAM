package com.edu.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.platform.entity.Correction;

public interface CorrectionService extends IService<Correction> {
    
    /**
     * 提交答案并触发批改
     */
    Correction submitAndCorrect(String homeworkId, String studentId, String questionId, String studentAnswer);
}
