package com.comercio.precios.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Comprueba que springdoc expone el documento OpenAPI y que declara el path de precios.
 *
 * <p>Comparte deliberadamente la anotación {@link SpringBootTest} con {@code ControladorPrecioTest}
 * para reutilizar el {@code ApplicationContext} cacheado por Spring y evitar un segundo arranque;
 * aislarlo con {@code @WebMvcTest} exigiría registrar manualmente los beans de springdoc y no
 * compensa el coste frente al ahorro de una suite de integración ya compacta.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiDocsTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /v3/api-docs responde 200 y declara el path /api/precios")
    void openApiIncluyeEndpointPrecios() throws Exception {
        var result =
                mockMvc.perform(MockMvcRequestBuilders.get("/v3/api-docs"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(root.path("paths").has("/api/precios")).isTrue();
    }
}
