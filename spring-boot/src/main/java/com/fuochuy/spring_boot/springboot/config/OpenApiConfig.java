package com.fuochuy.spring_boot.springboot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "Authorization",
        description = "Access token", scheme = "bearer", bearerFormat = "JWT")
@OpenAPIDefinition(
        info = @Info(title = "Spring boot API Definition", version = "v1"),
        security = {@SecurityRequirement(name = "Authorization")}
)
public class OpenApiConfig {
}
