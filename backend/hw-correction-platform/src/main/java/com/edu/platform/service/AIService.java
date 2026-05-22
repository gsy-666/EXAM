package com.edu.platform.service;

import java.math.BigDecimal;

public interface AIService {

  /**
   * 智能批改主观题
   *
   * @param studentAnswer  学生答案
   * @param standardAnswer 标准答案
   * @param maxScore       满分
   * @param ruleConfig     批改规则配置
   * @return 批改结果JSON (包含score和comment)
   */
  String autoCorrectSubjective(String studentAnswer, String standardAnswer, BigDecimal maxScore, String ruleConfig);
}
