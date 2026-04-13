package com.comercio.precios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Spring Boot del servicio de consulta de precios.
 */
@SpringBootApplication
public class PreciosApplication {

    /**
     * Arranca el contexto de Spring y el servidor embebido.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SpringApplication.run(PreciosApplication.class, args);
    }
}
