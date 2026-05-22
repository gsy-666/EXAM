package com.edu.platform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("高校智能作业批改与反馈平台 API接口文档")
            .version("1.0.0")
            .description("本项目使用 Spring Boot 3 + Knife4j + OpenAPI 3 规范构建的接口文档")
            .contact(new Contact().name("后端研发团队").url("").email("")));
  }
}
