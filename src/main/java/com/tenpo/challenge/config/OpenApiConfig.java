package com.tenpo.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI/Swagger documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates the OpenAPI configuration with API metadata.
     * @return configured OpenAPI instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tenpo Challenge API"
                        )
                        .version("1.0")
                        .description(
                            "API Documentation for"
                                + " the Tenpo Challenge application"
                        )
                );
    }
}
