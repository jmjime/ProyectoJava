package com.comercio.precios.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Modelo de dominio inmutable que representa un precio aplicable a un producto en una cadena
 * dentro de un intervalo temporal.
 *
 * <p>El canonical constructor valida los invariantes de negocio del tipo:
 * <ul>
 *   <li>Ningún atributo puede ser nulo.</li>
 *   <li>{@code fechaInicio} debe ser anterior o igual a {@code fechaFin}.</li>
 *   <li>{@code precioFinal} no puede ser negativo.</li>
 *   <li>{@code moneda} debe constar de exactamente tres letras mayúsculas ASCII
 *       (coherencia sintáctica con ISO 4217; no se valida el catálogo completo).</li>
 * </ul>
 *
 * @param idProducto  identificador del producto
 * @param idCadena    identificador de la cadena (marca)
 * @param tarifa      identificador de la lista de precios / tarifa aplicada
 * @param fechaInicio inicio del intervalo de vigencia (inclusive)
 * @param fechaFin    fin del intervalo de vigencia (inclusive)
 * @param precioFinal importe final (PVP)
 * @param moneda      código ISO 4217 de la moneda (p. ej. {@code EUR})
 */
public record Precio(
        Long idProducto,
        Long idCadena,
        Long tarifa,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        BigDecimal precioFinal,
        String moneda) {

    public Precio {
        Objects.requireNonNull(idProducto, "idProducto");
        Objects.requireNonNull(idCadena, "idCadena");
        Objects.requireNonNull(tarifa, "tarifa");
        Objects.requireNonNull(fechaInicio, "fechaInicio");
        Objects.requireNonNull(fechaFin, "fechaFin");
        Objects.requireNonNull(precioFinal, "precioFinal");
        Objects.requireNonNull(moneda, "moneda");
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException(
                    "fechaInicio (" + fechaInicio + ") debe ser anterior o igual a fechaFin (" + fechaFin + ")");
        }
        if (precioFinal.signum() < 0) {
            throw new IllegalArgumentException("precioFinal no puede ser negativo: " + precioFinal);
        }
        if (!moneda.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException(
                    "moneda debe constar de tres letras mayúsculas ASCII (ISO 4217): '" + moneda + "'");
        }
    }
}
