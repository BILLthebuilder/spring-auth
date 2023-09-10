package com.unknown.configuration.docs;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("AUTHENTICATION API")
                        .description("An authentication API")
                        .version("v0.0.1")
                        .license(new License().name("MIT").url("https://github.com/BILLthebuilder/lending-repayment-api/blob/main/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://github.com/BILLthebuilder/lending-repayment-api/blob/main/README.md"));
    }

}

