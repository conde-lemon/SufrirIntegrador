package com.travel4u.demo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debe mostrar las reservas del usuario autenticado")
    @WithMockUser(username = "cliente@travel4u.com") // Simula que el usuario de prueba está logueado
    void testShowMisReservasPage_ConReservasExistentes() throws Exception {
        // Los datos de este usuario se cargan desde data.sql

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk()) // Espera una respuesta 200 OK
                .andExpect(view().name("reservas")) // Espera que se renderice la vista 'reservas.html'
                .andExpect(model().attributeExists("reservas")) // Verifica que el modelo contenga el atributo 'reservas'
                .andExpect(model().attribute("reservas", hasSize(2))) // Verifica que hay 2 reservas para este usuario
                .andExpect(content().string(containsString("Vuelo a Madrid"))) // Verifica que el nombre de una reserva aparece en el HTML
                .andExpect(content().string(containsString("Vuelo Económico a Cusco"))); // Verifica que la otra también
    }

    @Test
    @DisplayName("Debe mostrar un mensaje cuando el usuario no tiene reservas")
    @WithMockUser(username = "usuario.sin.reservas@test.com") // Un usuario que no existe en data.sql
    void testShowMisReservasPage_SinReservas() throws Exception {
        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservas"))
                .andExpect(model().attribute("reservas", hasSize(0))) // Verifica que la lista de reservas está vacía
                .andExpect(content().string(containsString("Aún no tienes reservas"))); // Verifica que se muestra el mensaje correcto
    }
}