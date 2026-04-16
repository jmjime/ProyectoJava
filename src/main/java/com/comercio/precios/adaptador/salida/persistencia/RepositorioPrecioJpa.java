package com.comercio.precios.adaptador.salida.persistencia;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio Spring Data JPA para consultas sobre la tabla de precios.
 */
public interface RepositorioPrecioJpa extends JpaRepository<EntidadPrecioJpa, Long> {

    /**
     * Obtiene las tarifas aplicables ordenadas por prioridad descendente (la primera es la ganadora).
     *
     * @param idCadena    cadena
     * @param idProducto  producto
     * @param fecha       instante de aplicación
     * @param pageable    limita a una fila para eficiencia
     * @return página con como máximo un elemento (según {@code pageable})
     */
    @Query(
            """
            SELECT p FROM EntidadPrecioJpa p
            WHERE p.idCadena = :idCadena
              AND p.idProducto = :idProducto
              AND :fecha >= p.fechaInicio
              AND :fecha <= p.fechaFin
            ORDER BY p.prioridad DESC
            """)
    Page<EntidadPrecioJpa> buscarAplicables(
            @Param("idCadena") Long idCadena,
            @Param("idProducto") Long idProducto,
            @Param("fecha") LocalDateTime fecha,
            Pageable pageable);
}
