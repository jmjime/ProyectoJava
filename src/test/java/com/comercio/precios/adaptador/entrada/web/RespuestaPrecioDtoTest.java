package com.comercio.precios.adaptador.entrada.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.comercio.precios.dominio.Precio;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RespuestaPrecioDtoTest {

    @Test
    @DisplayName("desdeDominio copia todos los campos al DTO de salida")
    void desdeDominio_mapeaCampos() {
        var inicio = LocalDateTime.of(2020, 6, 14, 0, 0);
        var fin = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
        var dominio =
                new Precio(35455L, 1L, 1L, inicio, fin, new BigDecimal("35.50"), "EUR");

        RespuestaPrecioDto dto = RespuestaPrecioDto.desdeDominio(dominio);

        assertThat(dto.idProducto()).isEqualTo(35455L);
        assertThat(dto.idCadena()).isEqualTo(1L);
        assertThat(dto.tarifa()).isEqualTo(1L);
        assertThat(dto.fechaInicio()).isEqualTo(inicio);
        assertThat(dto.fechaFin()).isEqualTo(fin);
        assertThat(dto.precio()).isEqualByComparingTo("35.50");
        assertThat(dto.moneda()).isEqualTo("EUR");
    }
}
