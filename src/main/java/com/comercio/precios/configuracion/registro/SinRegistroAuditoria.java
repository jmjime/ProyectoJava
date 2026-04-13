package com.comercio.precios.configuracion.registro;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca un método para excluirlo del aspecto de registro transversal (AOP).
 *
 * <p>Útil para reducir ruido en métodos muy frecuentes o generados.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SinRegistroAuditoria {}
