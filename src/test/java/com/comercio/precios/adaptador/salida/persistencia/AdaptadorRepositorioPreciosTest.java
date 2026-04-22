package com.comercio.precios.adaptador.salida.persistencia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.comercio.precios.dominio.Precio;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Prueba unitaria del adaptador de persistencia: verifica el mapeo entidad → dominio campo a
 * campo y el orden en el que se propagan los parámetros al repositorio. El adaptador se apoya en
 * el método alias {@code buscarPrecioAplicable(idProducto, idCadena, fecha)} expuesto por la
 * interfaz de Spring Data; la duplicación interna de {@code fecha} al método derivado se valida
 * a nivel de integración en {@link RepositorioPrecioJpaTest}.
 */
@ExtendWith(MockitoExtension.class)
class AdaptadorRepositorioPreciosTest {

    @Mock private RepositorioPrecioJpa repositorioPrecioJpa;

    @InjectMocks private AdaptadorRepositorioPrecios adaptador;

    @Test
    @DisplayName("Mapea todos los campos de la entidad al dominio")
    void buscarPrecioAplicable_mapeoCompleto() {
        var fechaInicio = LocalDateTime.of(2020, 6, 14, 0, 0);
        var fechaFin = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
        var entidad =
                new EntidadPrecioJpa(
                        123L,
                        1L,
                        fechaInicio,
                        fechaFin,
                        1L,
                        35455L,
                        0,
                        new BigDecimal("35.50"),
                        "EUR");
        when(repositorioPrecioJpa.buscarPrecioAplicable(anyLong(), anyLong(), any()))
                .thenReturn(Optional.of(entidad));

        Optional<Precio> resultado =
                adaptador.buscarPrecioAplicable(
                        35455L, 1L, LocalDateTime.of(2020, 6, 14, 10, 0));

        assertThat(resultado).isPresent();
        Precio precio = resultado.get();
        assertThat(precio.idProducto()).isEqualTo(35455L);
        assertThat(precio.idCadena()).isEqualTo(1L);
        assertThat(precio.tarifa()).isEqualTo(1L);
        assertThat(precio.fechaInicio()).isEqualTo(fechaInicio);
        assertThat(precio.fechaFin()).isEqualTo(fechaFin);
        assertThat(precio.precioFinal()).isEqualByComparingTo("35.50");
        assertThat(precio.moneda()).isEqualTo("EUR");
    }

    @Test
    @DisplayName("Propaga los parámetros al repositorio en el orden (idProducto, idCadena, fecha)")
    void buscarPrecioAplicable_ordenArgumentos() {
        var fechaAplicacion = LocalDateTime.of(2020, 6, 14, 10, 0);

        adaptador.buscarPrecioAplicable(10L, 2L, fechaAplicacion);

        ArgumentCaptor<Long> idProductoCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> idCadenaCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<LocalDateTime> fechaCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(repositorioPrecioJpa)
                .buscarPrecioAplicable(
                        idProductoCaptor.capture(),
                        idCadenaCaptor.capture(),
                        fechaCaptor.capture());
        assertThat(idProductoCaptor.getValue()).isEqualTo(10L);
        assertThat(idCadenaCaptor.getValue()).isEqualTo(2L);
        assertThat(fechaCaptor.getValue()).isEqualTo(fechaAplicacion);
    }

    @Test
    @DisplayName("Propaga Optional.empty() cuando el repositorio no halla filas")
    void buscarPrecioAplicable_sinResultados() {
        Optional<Precio> resultado =
                adaptador.buscarPrecioAplicable(
                        35455L, 1L, LocalDateTime.of(2020, 6, 14, 10, 0));

        assertThat(resultado).isEmpty();
    }
}
