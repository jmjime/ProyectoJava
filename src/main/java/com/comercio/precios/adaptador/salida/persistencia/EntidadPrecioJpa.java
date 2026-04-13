package com.comercio.precios.adaptador.salida.persistencia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA que mapea la tabla de precios en H2.
 */
@Entity
@Table(name = "precios")
@Getter
@Setter
@NoArgsConstructor
public class EntidadPrecioJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cadena", nullable = false)
    private Long idCadena;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "lista_precios", nullable = false)
    private Long listaPrecios;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @Column(name = "prioridad", nullable = false)
    private Integer prioridad;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "moneda", nullable = false, length = 3)
    private String moneda;
}
