package com.comercio.precios.aplicacion;

import com.comercio.precios.dominio.Precio;
import com.comercio.precios.dominio.puerto.entrada.ConsultarPrecioAplicable;
import com.comercio.precios.dominio.puerto.salida.PuertoRepositorioPrecios;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

/**
 * Caso de uso que implementa el puerto de entrada {@link ConsultarPrecioAplicable} delegando en
 * el puerto de salida {@link PuertoRepositorioPrecios}. Esta clase es deliberadamente
 * <em>Spring-free</em>: su registro como bean se realiza en
 * {@code com.comercio.precios.configuracion.BeansAplicacion} mediante {@code @Bean}.
 */
@RequiredArgsConstructor
public class CasoUsoConsultarPrecioAplicable implements ConsultarPrecioAplicable {

    private final PuertoRepositorioPrecios puertoRepositorioPrecios;

    /**
     * {@inheritDoc}
     */
    @Override
    public Precio consultar(Long idProducto, Long idCadena, LocalDateTime fechaAplicacion) {
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
