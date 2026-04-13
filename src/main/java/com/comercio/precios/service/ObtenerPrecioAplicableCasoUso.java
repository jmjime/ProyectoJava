package com.comercio.precios.service;

import com.comercio.precios.dominio.Precio;
import com.comercio.precios.dominio.PuertoRepositorioPrecios;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Caso de uso de aplicación: obtiene el precio aplicable delegando en el puerto de persistencia.
 */
@Service
@RequiredArgsConstructor
public class ObtenerPrecioAplicableCasoUso {

    private final PuertoRepositorioPrecios puertoRepositorioPrecios;

    /**
     * Ejecuta la consulta de negocio y devuelve el precio único aplicable.
     *
     * @param idProducto      identificador del producto
     * @param idCadena        identificador de la cadena
     * @param fechaAplicacion fecha y hora de aplicación del precio
     * @return modelo de dominio {@link Precio}
     * @throws PrecioNoEncontradoException si no hay tarifa que cubra la fecha
     */
    public Precio ejecutar(Long idProducto, Long idCadena, LocalDateTime fechaAplicacion) {
        return puertoRepositorioPrecios
                .buscarPrecioAplicable(idProducto, idCadena, fechaAplicacion)
                .orElseThrow(
                        () ->
                                new PrecioNoEncontradoException(
                                        "No existe precio aplicable para el producto "
                                                + idProducto
                                                + ", cadena "
                                                + idCadena
                                                + " en la fecha indicada."));
    }
}
