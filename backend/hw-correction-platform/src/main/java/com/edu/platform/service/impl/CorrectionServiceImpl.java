package com.edu.platform.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.platform.entity.Correction;
import com.edu.platform.entity.Question;
import com.edu.platform.mapper.CorrectionMapper;
import com.edu.platform.mapper.QuestionMapper;
import com.edu.platform.service.AIService;
import com.edu.platform.service.CorrectionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CorrectionServiceImpl extends ServiceImpl<CorrectionMapper, Correction> implements CorrectionService {

    private final QuestionMapper questionMapper;
    private final AIService aiService;

    public CorrectionServiceImpl(QuestionMapper questionMapper, AIService aiService) {
        this.questionMapper = questionMapper;
        this.aiService = aiService;
    }

    @Override
    public Correction submitAndCorrect(String homeworkId, String studentId, String questionId, String studentAnswer) {
        // 1. 保存提交记录
        Correction correction = new Correction();
        correction.setHomeworkId(homeworkId);
        correction.setStudentId(studentId);
        correction.setQuestionId(questionId);
        correction.setStudentAnswer(studentAnswer);
        correction.setCorrectionType(1); // 智能批改
        correction.setCorrectionTime(LocalDateTime.now());
        
        // 2. 获取题目信息
        Question question = questionMapper.selectById(questionId);
        if (question == null) {
            throw new RuntimeException("题目不存在");
        }

        // 3. 开始批改
        boolean isObjective = question.getQuestionType() <= 4; // 1-4为客观题
        if (isObjective) {
            // 客观题：字符精确匹配或部分包含逻辑
            if (question.getStandardAnswer().trim().equalsIgnoreCase(studentAnswer.trim())) {
                correction.setScore(question.getScore());
                correction.setRightFlag(1);
                correction.setComment("回答正确");
            } else {
                correction.setScore(BigDecimal.ZERO);
                correction.setRightFlag(0);
                correction.setComment("回答错误");
            }
        } else {
            // 主观题：调用第三方AI API
            String aiResultStr = aiService.autoCorrectSubjective(
                    studentAnswer, 
                    question.getStandardAnswer(), 
                    question.getScore(), 
                    question.getRuleConfig()
            );
            
            try {
                JSONObject res = JSONUtil.parseObj(aiResultStr);
                BigDecimal aiScore = res.getBigDecimal("score");
                String aiComment = res.getStr("comment");
                
                correction.setScore(aiScore);
                correction.setComment(aiComment);
                
                // 设置标志：全对、半对、错
                if (aiScore.compareTo(question.getScore()) >= 0) {
                    correction.setRightFlag(1);
                } else if (aiScore.compareTo(BigDecimal.ZERO) > 0) {
                    correction.setRightFlag(2);
                } else {
                    correction.setRightFlag(0);
                }
                
            } catch (Exception e) {
                // JSON解析失败等异常，回退处理
                correction.setScore(BigDecimal.ZERO);
                correction.setComment("AI批改结果异常，请等待人工复核。");
                correction.setRightFlag(0);
            }
        }

        // 4. 保存数据库
        this.save(correction);
        return correction;
    }
}
