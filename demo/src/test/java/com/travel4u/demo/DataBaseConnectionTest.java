package com.travel4u.demo;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para verificar la conexión a la base de datos
 * y el correcto funcionamiento de los repositorios (DAO).
 */
@SpringBootTest
@Transactional // <-- IMPORTANTE: Revierte los cambios en la BD después de cada test
class DatabaseConnectionTest {

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private IReservaDAO reservaDAO;

    @Test
    void testCrearYRecuperarUsuarioYReservas() {
        // --- 1. Creación de Datos de Prueba ---

        // Crear un nuevo usuario
        Usuario testUser = new Usuario();
        testUser.setNombres("Test");
        testUser.setApellidos("User");
        testUser.setEmail("test.user@example.com");
        testUser.setPassword("password123"); // En un test, no importa si no está encriptada
        testUser.setRol("USUARIO");
        testUser.setActivo(true);
        testUser.setFechaRegistro(LocalDateTime.now());

        // Guardar el usuario en la BD
        Usuario savedUser = usuarioDAO.save(testUser);
        assertNotNull(savedUser.getIdUsuario(), "El ID del usuario no debería ser nulo después de guardar.");

        // Crear una reserva para ese usuario
        Reserva testReserva = new Reserva();
        testReserva.setUsuario(savedUser);
        testReserva.setEstado("CONFIRMADA");
        testReserva.setTotal(new BigDecimal("1250.75"));
        testReserva.setFechaInicio(LocalDateTime.now().plusDays(10));
        testReserva.setFechaFin(LocalDateTime.now().plusDays(17));
        testReserva.setMoneda("USD");

        // Guardar la reserva en la BD
        Reserva savedReserva = reservaDAO.save(testReserva);
        assertNotNull(savedReserva.getIdReserva(), "El ID de la reserva no debería ser nulo después de guardar.");

        // --- 2. Verificación y Aserciones ---

        // Recuperar el usuario por su email
        Usuario foundUser = usuarioDAO.findByEmail("test.user@example.com")
                .orElse(null);

        // Verificar que el usuario fue encontrado
        assertNotNull(foundUser, "Se debería encontrar el usuario por su email.");
        assertEquals("Test User", foundUser.getNombres() + " " + foundUser.getApellidos());

        // Recuperar las reservas para ese usuario
        List<Reserva> foundReservas = reservaDAO.findByUsuario(foundUser);

        // Verificar que se encontró la reserva
        assertNotNull(foundReservas, "La lista de reservas no debería ser nula.");
        assertFalse(foundReservas.isEmpty(), "La lista de reservas no debería estar vacía.");
        assertEquals(1, foundReservas.size(), "Debería haber exactamente una reserva para el usuario.");

        // Verificar los datos de la reserva encontrada
        Reserva retrievedReserva = foundReservas.get(0);
        assertEquals("CONFIRMADA", retrievedReserva.getEstado());
        // Usamos compareTo para BigDecimal
        assertEquals(0, new BigDecimal("1250.75").compareTo(retrievedReserva.getTotal()));
        assertEquals(savedUser.getIdUsuario(), retrievedReserva.getUsuario().getIdUsuario(), "La reserva debe pertenecer al usuario correcto.");

        System.out.println("✅ Test de Base de Datos Exitoso: Se creó, guardó y recuperó un usuario y su reserva correctamente.");
    }
}