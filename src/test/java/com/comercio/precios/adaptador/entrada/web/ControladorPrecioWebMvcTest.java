package com.comercio.precios.adaptador.entrada.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.comercio.precios.aplicacion.PrecioNoEncontradoException;
import com.comercio.precios.dominio.puerto.entrada.ConsultarPrecioAplicable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ControladorPrecio.class)
@Import(ManejadorExcepcionesGlobal.class)
@ActiveProfiles("test")
class ControladorPrecioWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsultarPrecioAplicable consultarPrecioAplicable;

    @Test
    @DisplayName("WebMvc: PrecioNoEncontradoException → 404 JSON con mensaje")
    void precioNoEncontrado_responde404() throws Exception {
        when(consultarPrecioAplicable.consultar(anyLong(), anyLong(), any()))
                .thenThrow(new PrecioNoEncontradoException("No existe precio aplicable de prueba"));

        mockMvc.perform(
                        get("/api/precios")
                                .param("fechaAplicacion", "2020-01-01T12:00:00")
                                .param("idProducto", "1")
                                .param("idCadena", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe precio aplicable de prueba"));
    }
}
