package com.comercio.precios.adaptador.salida.persistencia;

import com.comercio.precios.dominio.Precio;
import com.comercio.precios.dominio.puerto.salida.PuertoRepositorioPrecios;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida que implementa el puerto de dominio usando JPA.
 */
@Component
@RequiredArgsConstructor
public class AdaptadorRepositorioPrecios implements PuertoRepositorioPrecios {

    private final RepositorioPrecioJpa repositorioPrecioJpa;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Precio> buscarPrecioAplicable(
            Long idProducto, Long idCadena, LocalDateTime fechaAplicacion) {
        return repositorioPrecioJpa
                .buscarPrecioAplicable(idProducto, idCadena, fechaAplicacion)
                .map(this::mapearADominio);
    }

    private Precio mapearADominio(EntidadPrecioJpa entidad) {
        return new Precio(
                entidad.getIdProducto(),
                entidad.getIdCadena(),
                entidad.getListaPrecios(),
                entidad.getFechaInicio(),
                entidad.getFechaFin(),
                entidad.getPrecio(),
                entidad.getMoneda());
    }
}
