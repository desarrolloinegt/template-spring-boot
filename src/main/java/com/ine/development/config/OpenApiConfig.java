package com.ine.development.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("DESARROLLO INE")
                        .description("Template para los permisos.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo INE")
                                .email("ine@ine.gob.gt")))
                .components(new Components());
    }

    @Bean
    public GroupedOpenApi securityApi() {
        return GroupedOpenApi.builder()
                .group("Seguridad")
                .pathsToMatch(
                        "/api/v1/auth/**",
                        "/api/v1/users/**"
                )
                .packagesToScan("com.ine.development.controllers")
                .addOpenApiCustomizer(o -> o.setInfo(new Info()
                                .title("Seguridad")
                                .version("v1.0.0")
                                .description("Modulo demo para la plantilla de Spring boot.")
                                .contact(new Contact()
                                        .name("Equipo de Desarrollo INE")
                                        .email("cdcorzo@ine.gob.gt")
                                        .url("https://github.com/desarrolloinegt")
                                )
                        )
                )
                .build();
    }
}
