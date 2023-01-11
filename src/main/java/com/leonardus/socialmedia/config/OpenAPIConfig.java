package com.leonardus.socialmedia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info().title("Social-Media")
                .version("1.0.0")
                .description("Social-Media API allows you creating users, creating posts and commenting on posts"));
    }
}
