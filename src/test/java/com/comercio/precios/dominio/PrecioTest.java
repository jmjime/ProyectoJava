package com.comercio.precios.dominio;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PrecioTest {

    @Test
    @DisplayName("Rechaza idProducto nulo")
    void constructor_idProductoNulo() {
        var inicio = LocalDateTime.now();
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        null,
                                        1L,
                                        1L,
                                        inicio,
                                        inicio.plusDays(1),
                                        BigDecimal.ONE,
                                        "EUR"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("idProducto");
    }

    @Test
    @DisplayName("Rechaza idCadena nulo")
    void constructor_idCadenaNulo() {
        var inicio = LocalDateTime.now();
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        1L,
                                        null,
                                        1L,
                                        inicio,
                                        inicio.plusDays(1),
                                        BigDecimal.ONE,
                                        "EUR"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("idCadena");
    }

    @Test
    @DisplayName("Rechaza tarifa nula")
    void constructor_tarifaNula() {
        var inicio = LocalDateTime.now();
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        1L,
                                        1L,
                                        null,
                                        inicio,
                                        inicio.plusDays(1),
                                        BigDecimal.ONE,
                                        "EUR"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("tarifa");
    }

    @Test
    @DisplayName("Rechaza precio final nulo")
    void constructor_precioFinalNulo() {
        var inicio = LocalDateTime.now();
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        1L,
                                        1L,
                                        1L,
                                        inicio,
                                        inicio.plusDays(1),
                                        null,
                                        "EUR"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("precioFinal");
    }

    @Test
    @DisplayName("Rechaza moneda nula")
    void constructor_monedaNula() {
        var inicio = LocalDateTime.now();
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        1L,
                                        1L,
                                        1L,
                                        inicio,
                                        inicio.plusDays(1),
                                        BigDecimal.ONE,
                                        null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("moneda");
    }

    @Test
    @DisplayName("Rechaza fechaInicio nula")
    void constructor_fechaInicioNula() {
        var fin = LocalDateTime.now().plusDays(1);
        assertThatThrownBy(
                        () -> new Precio(1L, 1L, 1L, null, fin, BigDecimal.ONE, "EUR"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("fechaInicio");
    }

    @Test
    @DisplayName("Rechaza fechaFin nula")
    void constructor_fechaFinNula() {
        var inicio = LocalDateTime.now();
        assertThatThrownBy(
                        () -> new Precio(1L, 1L, 1L, inicio, null, BigDecimal.ONE, "EUR"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("fechaFin");
    }

    @Test
    @DisplayName("Rechaza fechaInicio posterior a fechaFin")
    void constructor_fechasIncoherentes() {
        var inicio = LocalDateTime.of(2020, 6, 14, 12, 0);
        var fin = LocalDateTime.of(2020, 6, 14, 11, 59);
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        1L,
                                        1L,
                                        1L,
                                        inicio,
                                        fin,
                                        BigDecimal.ONE,
                                        "EUR"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fechaInicio")
                .hasMessageContaining("fechaFin");
    }

    @Test
    @DisplayName("Rechaza precioFinal negativo")
    void constructor_precioNegativo() {
        var inicio = LocalDateTime.of(2020, 6, 14, 0, 0);
        var fin = inicio.plusDays(1);
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        1L,
                                        1L,
                                        1L,
                                        inicio,
                                        fin,
                                        new BigDecimal("-0.01"),
                                        "EUR"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precioFinal");
    }

    @Test
    @DisplayName("Rechaza moneda con longitud distinta de 3")
    void constructor_monedaLongitudInvalida() {
        var inicio = LocalDateTime.of(2020, 6, 14, 0, 0);
        var fin = inicio.plusDays(1);
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        1L,
                                        1L,
                                        1L,
                                        inicio,
                                        fin,
                                        BigDecimal.ONE,
                                        "EUROS"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("moneda");
    }

    @Test
    @DisplayName("Rechaza moneda con minúsculas")
    void constructor_monedaMinusculas() {
        var inicio = LocalDateTime.of(2020, 6, 14, 0, 0);
        var fin = inicio.plusDays(1);
        assertThatThrownBy(
                        () ->
                                new Precio(
                                        1L,
                                        1L,
                                        1L,
                                        inicio,
                                        fin,
                                        BigDecimal.ONE,
                                        "eur"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("moneda");
    }
}
