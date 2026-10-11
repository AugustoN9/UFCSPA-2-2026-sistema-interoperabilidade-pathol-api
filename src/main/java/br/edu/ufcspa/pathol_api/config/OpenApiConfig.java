package br.edu.ufcspa.pathol_api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pathol API")
                        .version("1.0.0")
                        .description("API do Sistema Patológico e Epidemiológico"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("pathol-api-v1")
                .packagesToScan("br.edu.ufcspa.pathol_api.controller")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}