package com.edu.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.platform.common.Result;
import com.edu.platform.entity.Homework;
import com.edu.platform.entity.Question;
import com.edu.platform.service.HomeworkService;
import com.edu.platform.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "教师端功能", description = "供教师发布作业、维护题库查看成绩等使用")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final HomeworkService homeworkService;
    private final QuestionService questionService;

    public TeacherController(HomeworkService homeworkService, QuestionService questionService) {
        this.homeworkService = homeworkService;
        this.questionService = questionService;
    }

    @Operation(summary = "发布新作业")
    @PostMapping("/homework/publish")
    public Result<String> publishHomework(@RequestBody Homework homework) {
        boolean saved = homeworkService.save(homework);
        return saved ? Result.success("发布作业成功", homework.getHomeworkId()) : Result.error("发布失败");
    }

    @Operation(summary = "录入新题目到对应的作业中")
    @PostMapping("/question/add")
    public Result<String> addQuestion(@RequestBody Question question) {
        boolean saved = questionService.save(question);
        return saved ? Result.success("题目创建成功", question.getQuestionId()) : Result.error("题目创建失败");
    }

    @Operation(summary = "查看教师发布的全部作业列表")
    @GetMapping("/homework/list")
    public Result<List<Homework>> getHomeworkList(@RequestParam String teacherId) {
        List<Homework> list = homeworkService.list(new LambdaQueryWrapper<Homework>()
                .eq(Homework::getTeacherId, teacherId)
                .orderByDesc(Homework::getCreateTime));
        return Result.success(list);
    }

    @Operation(summary = "查询作业包含的题目列表")
    @GetMapping("/question/list")
    public Result<List<Question>> getQuestionList(@RequestParam String homeworkId) {
        List<Question> list = questionService.list(new LambdaQueryWrapper<Question>()
                .eq(Question::getHomeworkId, homeworkId));
        return Result.success(list);
    }
}