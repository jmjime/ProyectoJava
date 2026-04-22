package com.comercio.precios.adaptador.salida.persistencia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que mapea la tabla de precios en H2.
 *
 * <p>Deliberadamente sin {@code @Setter} público: el estado es poblado por Hibernate mediante
 * acceso a campos y por tests del propio paquete vía el constructor con todos los argumentos.
 * El constructor sin argumentos es {@code protected} porque solo lo necesita el proveedor JPA.
 */
@Entity
@Table(
        name = "precios",
        indexes = {
            @Index(
                    name = "idx_precios_cadena_producto_prioridad",
                    columnList = "id_cadena,id_producto,prioridad")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
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
