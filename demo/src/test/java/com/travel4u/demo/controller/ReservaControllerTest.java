package com.travel4u.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
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
                .andExpect(model().attributeExists("reservas")); // Verifica que el modelo contenga el atributo 'reservas'
    }

    @Test
    @DisplayName("Debe mostrar un mensaje cuando el usuario no tiene reservas")
    @WithMockUser(username = "admin@travel4u.com") // Usuario admin que existe
    void testShowMisReservasPage_SinReservas() throws Exception {
        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservas"))
                .andExpect(model().attributeExists("reservas")); // Solo verifica que existe el atributo
    }
}