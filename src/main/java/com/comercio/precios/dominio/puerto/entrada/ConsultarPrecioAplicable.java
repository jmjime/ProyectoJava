package com.comercio.precios.dominio.puerto.entrada;

import com.comercio.precios.dominio.Precio;
import java.time.LocalDateTime;

/**
 * Puerto de entrada del dominio: consulta del precio aplicable a un producto en una cadena
 * para una fecha concreta.
 *
 * <p>Es el contrato que consumen los adaptadores de entrada (REST, batch, CLI, …). La
 * implementación del caso de uso vive en la capa de aplicación y se registra como bean en
 * {@code com.comercio.precios.configuracion}.
 */
public interface ConsultarPrecioAplicable {

    /**
     * Resuelve la tarifa aplicable en la fecha indicada aplicando la regla de desempate del
     * dominio ({@code prioridad DESC}, {@code fechaInicio DESC}, {@code id DESC}).
     *
     * @param idProducto      identificador del producto
     * @param idCadena        identificador de la cadena (marca)
     * @param fechaAplicacion instante de consulta (debe caer entre inicio y fin de la tarifa, inclusive)
     * @return precio aplicable de dominio
     * @throws com.comercio.precios.aplicacion.PrecioNoEncontradoException si ninguna tarifa cubre la fecha
     */
    Precio consultar(Long idProducto, Long idCadena, LocalDateTime fechaAplicacion);
}
