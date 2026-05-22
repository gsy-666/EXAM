package com.edu.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_question")
public class Question {
  @TableId(type = IdType.ASSIGN_ID)
  private String questionId;
  private String homeworkId;
  private Integer questionType;
  private String content;
  private String standardAnswer;
  private BigDecimal score;
  private String ruleConfig;
  private String knowledgeTag;
  private LocalDateTime createTime;
}
