package com.be_provocation.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }
    private Info apiInfo() {
        return new Info()
                .title("기숙사 룸메 매칭 플랫폼 REST API") // API의 제목
                .description("made by 백엔드의 도발 Backend Team") // API에 대한 설명
                .contact(new Contact()
                        .name("BE Github")
                        .url("https://github.com/KGU-2024-OpenSource/BE")) // BE 레포지토리 주소
                .version("1.0.0"); // API의 버전
    }
}