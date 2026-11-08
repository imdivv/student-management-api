package com.example.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentApiDocumentation() {

        return new OpenAPI()
                .info(new Info()
                        .title("Student Management API")
                        .description("A RESTful API for managing student records. "
                                + "Supports CRUD operations, searching, filtering, "
                                + "sorting and pagination.")
                        .version("1.0.0"));
    }
}
