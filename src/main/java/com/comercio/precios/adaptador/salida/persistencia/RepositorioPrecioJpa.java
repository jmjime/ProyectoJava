package com.comercio.precios.adaptador.salida.persistencia;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data JPA para consultas sobre la tabla de precios.
 */
public interface RepositorioPrecioJpa extends JpaRepository<EntidadPrecioJpa, Long> {

    /**
     * Método derivado que resuelve la tarifa aplicable con una única {@code SELECT ... LIMIT 1}.
     * No se emite {@code count} adicional (no devuelve {@link org.springframework.data.domain.Page}).
     *
     * <p>Desempate determinista: mayor {@code prioridad} primero; a igualdad, {@code fechaInicio}
     * más reciente; a igualdad, mayor {@code id} (última fila persistida).
     *
     * @param idProducto  identificador del producto
     * @param idCadena    identificador de la cadena
     * @param fechaInicio cota superior para {@code fecha_inicio} (normalmente la fecha de aplicación)
     * @param fechaFin    cota inferior para {@code fecha_fin} (normalmente la misma fecha de aplicación)
     * @return tarifa aplicable o {@link Optional#empty()} si ninguna vigencia cubre la fecha
     */
    Optional<EntidadPrecioJpa>
            findFirstByIdProductoAndIdCadenaAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByPrioridadDescFechaInicioDescIdDesc(
                    Long idProducto, Long idCadena, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Alias con firma corta del método derivado. Pasa la misma fecha como cota superior de
     * {@code fecha_inicio} y cota inferior de {@code fecha_fin} para seleccionar las tarifas
     * cuya vigencia contiene al instante indicado.
     *
     * @param idProducto identificador del producto
     * @param idCadena   identificador de la cadena
     * @param fecha      instante de aplicación
     * @return tarifa aplicable o vacío si no existe
     */
    default Optional<EntidadPrecioJpa> buscarPrecioAplicable(
            Long idProducto, Long idCadena, LocalDateTime fecha) {
        return findFirstByIdProductoAndIdCadenaAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualOrderByPrioridadDescFechaInicioDescIdDesc(
                idProducto, idCadena, fecha, fecha);
    }
}
