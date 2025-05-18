package com.example.assessment.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI recordOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Record API")
                        .description("API to search and update records")
                        .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("record-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
