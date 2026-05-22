package com.edu.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_correction")
public class Correction {
    @TableId(type = IdType.ASSIGN_ID)
    private String correctionId;
    private String homeworkId;
    private String studentId;
    private String questionId;
    private String studentAnswer;
    private BigDecimal score;
    private String comment;
    private Integer rightFlag;
    private Integer correctionType;
    private LocalDateTime correctionTime;
}
