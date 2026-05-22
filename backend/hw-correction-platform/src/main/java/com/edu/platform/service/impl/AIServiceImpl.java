package com.edu.platform.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.edu.platform.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AIServiceImpl implements AIService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    @Override
    public String autoCorrectSubjective(String studentAnswer, String standardAnswer, BigDecimal maxScore, String ruleConfig) {
        log.info("调用第三方AI进行阅卷: {}", studentAnswer);

        // 构建Prompt
        String prompt = String.format("你是一个专业的作业批改助手。请根据以下信息对学生的答案进行评分。\n" +
                        "标准答案：%s\n满分：%s\n批改要求：%s\n学生答案：%s\n" +
                        "请返回JSON格式：{\"score\": 评分, \"comment\": \"评语\"}",
                standardAnswer, maxScore.toString(), ruleConfig, studentAnswer);

        // 构建请求体 (以兼容OpenAI格式的第三方API为例，如豆包，智谱)
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("model", "auto-correction-model");
        reqBody.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", "你是一个智能批改助手，仅返回规定的JSON格式数据。");
                }},
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        });
        
        try {
            // 发起请求 (模拟，如果没有真实API key会失败，这里做好异常处理)
            String resultBody = HttpUtil.createPost(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .body(JSONUtil.toJsonStr(reqBody))
                    .execute().body();

            JSONObject resJson = JSONUtil.parseObj(resultBody);
            String aiContent = resJson.getJSONArray("choices")
                    .getJSONObject(0).getJSONObject("message").getStr("content");
                    
            return aiContent;
        } catch (Exception e) {
            log.error("AI 批改调用失败", e);
            // 失败时的默认返回
            return String.format("{\"score\": %s, \"comment\": \"AI批改失败，转为人工批改。\"}", "0");
        }
    }
}
