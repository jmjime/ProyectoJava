package com.comercio.precios.adaptador.salida.persistencia;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

/**
 * Pruebas de integración ligeras del repositorio con {@link DataJpaTest}. Fija el contrato del
 * método derivado {@code findFirst…OrderBy…} sin levantar el contexto MVC completo.
 *
 * <p>Habilita las estadísticas de Hibernate para verificar que la consulta aplicable se resuelve
 * con una única sentencia (sin {@code count} paginador), materializando el requisito §3.4 de la
 * capacidad {@code persistencia-eficiente}.
 */
@DataJpaTest
@Sql("/repositorio-precios-seed.sql")
@TestPropertySource(properties = {"spring.jpa.properties.hibernate.generate_statistics=true"})
class RepositorioPrecioJpaTest {

    @Autowired private RepositorioPrecioJpa repositorio;

    @Autowired private EntityManager entityManager;

    private Statistics statistics;

    @BeforeEach
    void reiniciarEstadisticas() {
        statistics =
                entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
    }

    @Test
    @DisplayName("Camino feliz: devuelve la única tarifa vigente y emite UNA consulta (sin count)")
    void buscarPrecioAplicable_caminoFeliz() {
        var resultado =
                repositorio.buscarPrecioAplicable(
                        10000L, 1L, LocalDateTime.of(2020, 9, 1, 12, 0));

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getListaPrecios()).isEqualTo(1L);
        assertThat(resultado.get().getPrecio()).isEqualByComparingTo("15.00");
        assertThat(statistics.getQueryExecutionCount())
                .as("El método findFirst… debe resolverse con una única SELECT ... LIMIT 1")
                .isEqualTo(1L);
    }

    @Test
    @DisplayName("Misma prioridad y solape: gana la tarifa con fechaInicio más reciente")
    void buscarPrecioAplicable_desempateFechaInicio() {
        var resultado =
                repositorio.buscarPrecioAplicable(
                        10001L, 1L, LocalDateTime.of(2020, 9, 1, 10, 0));

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getListaPrecios()).isEqualTo(31L);
        assertThat(resultado.get().getPrecio()).isEqualByComparingTo("99.99");
    }

    @Test
    @DisplayName("Misma prioridad e intervalo idéntico: gana la fila con id mayor")
    void buscarPrecioAplicable_desempateIdMayor() {
        var resultado =
                repositorio.buscarPrecioAplicable(
                        10002L, 1L, LocalDateTime.of(2020, 9, 2, 12, 0));

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getListaPrecios()).isEqualTo(41L);
        assertThat(resultado.get().getPrecio()).isEqualByComparingTo("20.00");
    }

    @Test
    @DisplayName("Sin tarifa aplicable devuelve Optional.empty()")
    void buscarPrecioAplicable_sinResultados() {
        var resultado =
                repositorio.buscarPrecioAplicable(
                        99999999L, 1L, LocalDateTime.of(2020, 9, 1, 12, 0));

        assertThat(resultado).isEmpty();
        assertThat(statistics.getQueryExecutionCount()).isEqualTo(1L);
    }
}
