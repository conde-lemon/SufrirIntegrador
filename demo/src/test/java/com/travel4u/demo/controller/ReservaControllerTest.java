package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Equipaje;
import com.travel4u.demo.reserva.repository.IEquipajeDAO;
import com.travel4u.demo.servicio.model.Servicio;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
public class ReservaControllerTest {

    @Autowired
    private IUsuarioDAO usuarioDAO;
    
    @Autowired
    private IServicioDAO servicioDAO;
    
    @Autowired
    private IEquipajeDAO equipajeDAO;

    private Usuario testUser;
    private Servicio testServicio;

    @BeforeEach
    public void setup() {
        // Crear usuario de prueba
        testUser = new Usuario();
        testUser.setEmail("test@travel4u.com");
        testUser.setPassword("test123");
        testUser.setRol("USER");
        testUser = usuarioDAO.save(testUser);

        // Crear servicio de prueba
        testServicio = new Servicio();
        testServicio.setTipoServicio("VUELO");
        testServicio.setNombre("Vuelo Test");
        testServicio.setOrigen("Lima");
        testServicio.setDestino("Madrid");
        testServicio.setPrecioBase(new BigDecimal("850.00"));
        testServicio.setDisponibilidad(100);
        testServicio.setDescripcion("Vuelo de prueba");
        testServicio = servicioDAO.save(testServicio);

        // Crear equipaje de prueba
        Equipaje equipaje = new Equipaje();
        equipaje.setTipo("Equipaje de mano");
        equipaje.setPrecio(new BigDecimal("50.00"));
        equipaje.setPesoMax(new BigDecimal("10.0"));
        equipaje.setDimensionLargo(new BigDecimal("55.0"));
        equipaje.setDimensionAncho(new BigDecimal("40.0"));
        equipaje.setDimensionAlto(new BigDecimal("20.0"));
        equipajeDAO.save(equipaje);
    }

    @Test
    public void testCrearUsuarioYServicio() {
        assertNotNull(testUser);
        assertNotNull(testUser.getIdUsuario());
        assertEquals("test@travel4u.com", testUser.getEmail());
        
        assertNotNull(testServicio);
        assertNotNull(testServicio.getIdServicio());
        assertEquals("VUELO", testServicio.getTipoServicio());
        
        System.out.println("✅ Test básico completado");
        System.out.println("Usuario ID: " + testUser.getIdUsuario());
        System.out.println("Servicio ID: " + testServicio.getIdServicio());
    }
}