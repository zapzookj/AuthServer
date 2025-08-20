package com.spring.authserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        final String schemeName = "BearerAuth";
        return new OpenAPI()
            .info(new Info()
                .title("Auth Server API")
                .version("1.0.0")
                .description("""
                        JWT 기반 인증/인가 API 문서입니다.
                        - 기본 주소: http://localhost:8080
                        - 토큰 형식: `Authorization: Bearer {JWT}`
                        """)
                .contact(new Contact().name("Auth Team"))
                .license(new License().name("Apache-2.0")))
            .addSecurityItem(new SecurityRequirement().addList(schemeName))
            .components(new Components().addSecuritySchemes(schemeName,
                new SecurityScheme()
                    .name(schemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}
