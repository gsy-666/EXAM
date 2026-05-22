package com.edu.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.platform.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}
