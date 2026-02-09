package com.victor.picpay.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swaggerConfiguration() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Desafio Backend PicPay")
                                .version("v1.0")
                                .description("Essa API proprõe uma possível solução para o desafio de backend do PicPay")
                                .contact(new Contact()
                                        .name("Victor Lito")
                                        .email("victor.lito.dev@gmail.com")
                                        .url("https://www.linkedin.com/in/victor-lito/"))
                )
                .externalDocs( new ExternalDocumentation()
                        .description("Repositório do Github")
                        .url("https://github.com/VictorHSLito/Desafio-Backend-PicPay")
                )
                .components(
                        new Components()
                                .addSecuritySchemes("Bearer Token",
                                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                );
    }
}
