package com.comercio.precios.configuracion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos OpenAPI 3 (título, descripción, versión) para Swagger UI y el documento {@code /v3/api-docs}.
 * Solo afecta a la documentación HTTP; no introduce lógica de negocio.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Define la información general del API expuesta por springdoc-openapi.
     *
     * @return modelo OpenAPI con bloque {@code info}
     */
    @Bean
    public OpenAPI servicioPreciosOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API de consulta de precios")
                                .description(
                                        "Microservicio que devuelve la tarifa y el precio aplicable a un producto de una"
                                                + " cadena en una fecha y hora concretas (H2 en memoria, arquitectura"
                                                + " hexagonal).")
                                .version("1.0.0"));
    }
}
