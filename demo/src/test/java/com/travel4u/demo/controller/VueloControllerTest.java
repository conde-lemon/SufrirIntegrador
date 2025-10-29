package com.travel4u.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql; // <-- IMPORTA ESTA CLASE
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/test-data.sql") // <-- AÑADE ESTA LÍNEA
class VueloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void testBuscarVuelos_CuandoHayResultados() throws Exception {
        // PRUEBA 1: Ahora esta prueba encontrará el vuelo que insertamos con el script SQL.
        mockMvc.perform(get("/vuelos/buscar")
                        .param("origen", "Peru")
                        .param("destino", "Espana"))
                .andExpect(status().isOk())
                .andExpect(view().name("vuelos-resultados"))
                .andExpect(model().attributeExists("vuelos"))
                .andExpect(model().attribute("vuelos", hasSize(1)))
                .andExpect(model().attribute("vuelos", hasItem(
                        hasProperty("nombre", is("Vuelo a Madrid"))
                )));
    }

    @Test
    @WithMockUser
    void testBuscarVuelos_CuandoNoHayResultados() throws Exception {
        // PRUEBA 2: No encontrará vuelos para esta ruta, pero el controlador
        // encontrará el "Vuelo a Buenos Aires" como sugerencia.
        mockMvc.perform(get("/vuelos/buscar")
                        .param("origen", "Brasil")
                        .param("destino", "Peru"))
                .andExpect(status().isOk())
                .andExpect(view().name("vuelos-resultados"))
                .andExpect(model().attribute("vuelos", hasSize(0)))
                .andExpect(model().attributeExists("sugerencias"))
                .andExpect(model().attribute("sugerencias", not(empty()))); // <-- Ahora esta aserción pasará
    }
}