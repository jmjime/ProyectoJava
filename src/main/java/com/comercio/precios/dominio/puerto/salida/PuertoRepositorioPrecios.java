package com.comercio.precios.dominio.puerto.salida;

import com.comercio.precios.dominio.Precio;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Puerto de salida del dominio: abstrae la obtención de precios aplicables desde la capa de persistencia.
 */
public interface PuertoRepositorioPrecios {

    /**
     * Busca el precio vigente para la fecha indicada, resolviendo empates por la mayor prioridad numérica.
     *
     * @param idProducto      identificador del producto
     * @param idCadena        identificador de la cadena
     * @param fechaAplicacion instante de consulta (debe caer entre inicio y fin de la tarifa, inclusive)
     * @return el precio aplicable, o vacío si ninguna tarifa cubre la fecha
     */
    Optional<Precio> buscarPrecioAplicable(Long idProducto, Long idCadena, LocalDateTime fechaAplicacion);
}
