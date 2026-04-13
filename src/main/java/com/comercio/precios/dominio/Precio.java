package com.comercio.precios.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa un precio aplicable a un producto en una cadena dentro de un intervalo temporal,
 * tal como se persiste en la tabla de tarifas.
 */
public final class Precio {

    private final Long idProducto;
    private final Long idCadena;
    private final Long tarifa;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFin;
    private final BigDecimal precioFinal;
    private final String moneda;

    /**
     * Crea un valor de precio aplicable con todos sus atributos de negocio.
     *
     * @param idProducto  identificador del producto
     * @param idCadena    identificador de la cadena (marca)
     * @param tarifa      identificador de la lista de precios / tarifa aplicada
     * @param fechaInicio inicio del intervalo de vigencia (inclusive)
     * @param fechaFin    fin del intervalo de vigencia (inclusive)
     * @param precioFinal importe final (PVP)
     * @param moneda      código ISO de la moneda (p. ej. EUR)
     */
    public Precio(
            Long idProducto,
            Long idCadena,
            Long tarifa,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            BigDecimal precioFinal,
            String moneda) {
        this.idProducto = Objects.requireNonNull(idProducto, "idProducto");
        this.idCadena = Objects.requireNonNull(idCadena, "idCadena");
        this.tarifa = Objects.requireNonNull(tarifa, "tarifa");
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "fechaInicio");
        this.fechaFin = Objects.requireNonNull(fechaFin, "fechaFin");
        this.precioFinal = Objects.requireNonNull(precioFinal, "precioFinal");
        this.moneda = Objects.requireNonNull(moneda, "moneda");
    }

    /**
     * @return identificador del producto
     */
    public Long getIdProducto() {
        return idProducto;
    }

    /**
     * @return identificador de la cadena
     */
    public Long getIdCadena() {
        return idCadena;
    }

    /**
     * @return identificador de la tarifa aplicable
     */
    public Long getTarifa() {
        return tarifa;
    }

    /**
     * @return fecha y hora de inicio de vigencia
     */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @return fecha y hora de fin de vigencia
     */
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    /**
     * @return precio final de venta
     */
    public BigDecimal getPrecioFinal() {
        return precioFinal;
    }

    /**
     * @return código ISO de moneda
     */
    public String getMoneda() {
        return moneda;
    }
}
