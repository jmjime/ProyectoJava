package com.comercio.precios.integracion;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Pruebas de integración del endpoint REST de consulta de precios con los escenarios del enunciado.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ControladorPrecioTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test 1: 14-jun 10:00 — solo aplica tarifa 1.
     */
    @Test
    @DisplayName("Test 1: 2020-06-14 10:00 producto 35455 cadena 1 → tarifa 1, 35.50 EUR")
    void test1_14Junio10h() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T10:00:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(35455))
                .andExpect(jsonPath("$.idCadena").value(1))
                .andExpect(jsonPath("$.tarifa").value(1))
                .andExpect(jsonPath("$.precio").value(35.50))
                .andExpect(jsonPath("$.moneda").value("EUR"))
                .andExpect(jsonPath("$.fechaInicio").value("2020-06-14T00:00:00"))
                .andExpect(jsonPath("$.fechaFin").value("2020-12-31T23:59:59"));
    }

    /**
     * Test 2: 14-jun 16:00 — tarifa 2 gana por mayor prioridad.
     */
    @Test
    @DisplayName("Test 2: 2020-06-14 16:00 → tarifa 2, 25.45 EUR")
    void test2_14Junio16h() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T16:00:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(35455))
                .andExpect(jsonPath("$.idCadena").value(1))
                .andExpect(jsonPath("$.tarifa").value(2))
                .andExpect(jsonPath("$.precio").value(25.45))
                .andExpect(jsonPath("$.moneda").value("EUR"))
                .andExpect(jsonPath("$.fechaInicio").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.fechaFin").value("2020-06-14T18:30:00"));
    }

    /**
     * Test 3: 14-jun 21:00 — tarifa 2 ya no vigente.
     */
    @Test
    @DisplayName("Test 3: 2020-06-14 21:00 → tarifa 1, 35.50 EUR")
    void test3_14Junio21h() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T21:00:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(35455))
                .andExpect(jsonPath("$.idCadena").value(1))
                .andExpect(jsonPath("$.tarifa").value(1))
                .andExpect(jsonPath("$.precio").value(35.50))
                .andExpect(jsonPath("$.moneda").value("EUR"));
    }

    /**
     * Test 4: 15-jun 10:00 — tarifa 3 con mayor prioridad que la base.
     */
    @Test
    @DisplayName("Test 4: 2020-06-15 10:00 → tarifa 3, 30.50 EUR")
    void test4_15Junio10h() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-15T10:00:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(35455))
                .andExpect(jsonPath("$.idCadena").value(1))
                .andExpect(jsonPath("$.tarifa").value(3))
                .andExpect(jsonPath("$.precio").value(30.50))
                .andExpect(jsonPath("$.moneda").value("EUR"))
                .andExpect(jsonPath("$.fechaInicio").value("2020-06-15T00:00:00"))
                .andExpect(jsonPath("$.fechaFin").value("2020-06-15T11:00:00"));
    }

    /**
     * Test 5: 16-jun 21:00 — tarifa 4 de largo alcance con mayor prioridad.
     */
    @Test
    @DisplayName("Test 5: 2020-06-16 21:00 → tarifa 4, 38.95 EUR")
    void test5_16Junio21h() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-16T21:00:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(35455))
                .andExpect(jsonPath("$.idCadena").value(1))
                .andExpect(jsonPath("$.tarifa").value(4))
                .andExpect(jsonPath("$.precio").value(38.95))
                .andExpect(jsonPath("$.moneda").value("EUR"))
                .andExpect(jsonPath("$.fechaInicio").value("2020-06-15T16:00:00"))
                .andExpect(jsonPath("$.fechaFin").value("2020-12-31T23:59:59"));
    }

    @Test
    @Sql("/test-desempate-misma-prioridad.sql")
    @DisplayName("Misma prioridad y solape: gana la tarifa con fecha_inicio más reciente")
    void desempate_mismaPrioridad_fechaInicioMasReciente() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-07-01T10:00:00")
                                .param("idProducto", "99901")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tarifa").value(11))
                .andExpect(jsonPath("$.precio").value(99.99))
                .andExpect(jsonPath("$.fechaInicio").value("2020-07-01T08:00:00"));
    }

    @Test
    @DisplayName("Sin tarifa aplicable responde 404 con mensaje en JSON")
    void sinPrecio_responde404() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2019-01-01T10:00:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value(containsString("35455")));
    }

    @Test
    @DisplayName("Falta fechaAplicacion → 400")
    void faltaFechaAplicacion_responde400() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Falta idProducto → 400")
    void faltaIdProducto_responde400() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T10:00:00")
                                .param("idCadena", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Falta idCadena → 400")
    void faltaIdCadena_responde400() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T10:00:00")
                                .param("idProducto", "35455"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("fechaAplicacion con formato no parseable → 400")
    void fechaAplicacionInvalida_responde400() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "no-es-fecha")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("idProducto no numérico → 400")
    void idProductoNoNumerico_responde400() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T10:00:00")
                                .param("idProducto", "xyz")
                                .param("idCadena", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("idCadena no numérico → 400")
    void idCadenaNoNumerico_responde400() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T10:00:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "no-numerico"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("/test-desempate-mismo-inicio-id.sql")
    @DisplayName("Misma prioridad e intervalo idéntico: gana la fila con id mayor")
    void desempate_mismoInicio_mayorId() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-08-01T12:00:00")
                                .param("idProducto", "99902")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tarifa").value(21))
                .andExpect(jsonPath("$.precio").value(20.00));
    }

    @Test
    @DisplayName("Instante exacto en fechaInicio de tarifa 2 (15:00) aplica tarifa 2")
    void limiteInferiorVigencia_tarifa2_inclusive() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T15:00:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tarifa").value(2))
                .andExpect(jsonPath("$.precio").value(25.45));
    }

    @Test
    @DisplayName("Instante exacto en fechaFin de tarifa 2 (18:30) aplica tarifa 2")
    void limiteSuperiorVigencia_tarifa2_inclusive() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T18:30:00")
                                .param("idProducto", "35455")
                                .param("idCadena", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tarifa").value(2))
                .andExpect(jsonPath("$.precio").value(25.45));
    }

    @Test
    @DisplayName("Producto inexistente en datos → 404")
    void productoSinFilas_responde404() throws Exception {
        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-06-14T10:00:00")
                                .param("idProducto", "999999999")
                                .param("idCadena", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value(containsString("999999999")));
    }
}
