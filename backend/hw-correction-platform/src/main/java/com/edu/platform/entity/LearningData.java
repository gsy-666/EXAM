package com.edu.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_learning_data")
public class LearningData {
  @TableId(type = IdType.ASSIGN_ID)
  private String dataId;
  private String userId;
  private String courseId;
  private String knowledgeTag;
  private BigDecimal correctRate;
  private Integer totalCount;
  private LocalDateTime updateTime;
}