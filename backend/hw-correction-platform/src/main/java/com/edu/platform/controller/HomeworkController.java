package com.edu.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.edu.platform.common.Result;
import com.edu.platform.entity.Correction;
import com.edu.platform.entity.Homework;
import com.edu.platform.service.CorrectionService;
import com.edu.platform.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "学生作业管理", description = "提供给学生端的作业相关接口（获取列表、提交、查询错题簿）")
@RestController
@RequestMapping("/api/student/homework")
public class HomeworkController {

  private final CorrectionService correctionService;
  private final HomeworkService homeworkService;

  public HomeworkController(CorrectionService correctionService, HomeworkService homeworkService) {
    this.correctionService = correctionService;
    this.homeworkService = homeworkService;
  }

  @Operation(summary = "提交作业答案", description = "学生提交单题答案，请求体会触发客观或主观题的自动化批改")
  @PostMapping("/submit")
  public Result<Correction> submitAnswer(@RequestBody Map<String, String> request) {
    String homeworkId = request.get("homeworkId");
    String studentId = request.get("studentId");
    String questionId = request.get("questionId");
    String studentAnswer = request.get("studentAnswer");

    Correction result = correctionService.submitAndCorrect(homeworkId, studentId, questionId, studentAnswer);
    return Result.success("提交并批改成功", result);
  }

  @Operation(summary = "获取本人需要完成的课程作业")
  @GetMapping("/list")
  public Result<List<Homework>> getHomeworkList(@RequestParam String courseId) {
    List<Homework> list = homeworkService.list(new LambdaQueryWrapper<Homework>()
        .eq(Homework::getCourseId, courseId)
        .orderByDesc(Homework::getCreateTime));
    return Result.success(list);
  }

  @Operation(summary = "获取错题本数据", description = "只查询当前学生被判定为错误（right_flag != 1）的记录")
  @GetMapping("/mistakes")
  public Result<List<Correction>> getMistakeBook(@RequestParam String studentId) {
    List<Correction> list = correctionService.list(new LambdaQueryWrapper<Correction>()
        .eq(Correction::getStudentId, studentId)
        .ne(Correction::getRightFlag, 1) // 不等于完全正确
        .orderByDesc(Correction::getCorrectionTime));
    return Result.success(list);
  }
}
