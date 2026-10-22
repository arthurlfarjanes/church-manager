package br.com.churchmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadados da documentação OpenAPI (Swagger UI em /api/swagger-ui.html).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI churchManagerOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Church Manager API")
                .description("API do Gerenciador de Paróquias: inventário, eventos, missas e pastorais.")
                .version("v0.0.1"));
    }
}
