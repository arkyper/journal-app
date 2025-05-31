package com.arkyper.journalApp.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new io.swagger.v3.oas.models.OpenAPI().info(
                new Info().title("Journal App APIs").description("By Saurabh Kumar"))
                // .tags(Arrays.asList(
                // new Tag().name("Public APIs").description("Public endpoints"),
                // new Tag().name("User APIs").description("User specific endpoints"),
                // new Tag().name("Journal APIs").description("Journal entry related
                // endpoints"),
                // new Tag().name("Admin APIs").description("Admin specific endpoints")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Local server")));
    }

}
