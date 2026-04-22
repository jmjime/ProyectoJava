package com.comercio.precios.aplicacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.comercio.precios.dominio.Precio;
import com.comercio.precios.dominio.puerto.salida.PuertoRepositorioPrecios;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CasoUsoConsultarPrecioAplicableTest {

    @Mock
    private PuertoRepositorioPrecios puertoRepositorioPrecios;

    @InjectMocks
    private CasoUsoConsultarPrecioAplicable casoUso;

    @Test
    @DisplayName("Devuelve el precio cuando el puerto encuentra una tarifa aplicable")
    void consultar_cuandoHayPrecio_devuelveDominio() {
        var fecha = LocalDateTime.of(2020, 6, 14, 10, 0);
        var precio =
                new Precio(
                        35455L,
                        1L,
                        1L,
                        fecha,
                        fecha.plusMonths(1),
                        new BigDecimal("35.50"),
                        "EUR");
        when(puertoRepositorioPrecios.buscarPrecioAplicable(35455L, 1L, fecha))
                .thenReturn(Optional.of(precio));

        var resultado = casoUso.consultar(35455L, 1L, fecha);

        assertThat(resultado).isSameAs(precio);
        verify(puertoRepositorioPrecios).buscarPrecioAplicable(35455L, 1L, fecha);
    }

    @Test
    @DisplayName("Lanza PrecioNoEncontradoException cuando el puerto no encuentra tarifa")
    void consultar_cuandoVacio_lanzaPrecioNoEncontrado() {
        var fecha = LocalDateTime.of(2020, 1, 1, 12, 0);
        when(puertoRepositorioPrecios.buscarPrecioAplicable(99L, 2L, fecha))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> casoUso.consultar(99L, 2L, fecha))
                .isInstanceOf(PrecioNoEncontradoException.class)
                .hasMessageContaining("99")
                .hasMessageContaining("2");
        verify(puertoRepositorioPrecios).buscarPrecioAplicable(99L, 2L, fecha);
    }
}
