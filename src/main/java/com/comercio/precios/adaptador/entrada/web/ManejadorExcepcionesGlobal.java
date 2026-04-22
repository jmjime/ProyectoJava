package com.comercio.precios.adaptador.entrada.web;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.comercio.precios.aplicacion.PrecioNoEncontradoException;

/**
 * Traduce excepciones de aplicación a respuestas HTTP coherentes.
 */
@RestControllerAdvice
@Slf4j
public class ManejadorExcepcionesGlobal {

    /**
     * Responde 404 cuando no hay precio aplicable.
     *
     * @param ex excepción de dominio/aplicación
     * @return cuerpo JSON con mensaje en castellano
     */
    @ExceptionHandler(PrecioNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarPrecioNoEncontrado(PrecioNoEncontradoException ex) {
        log.warn("Consulta sin resultado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensaje", ex.getMessage()));
    }
}
