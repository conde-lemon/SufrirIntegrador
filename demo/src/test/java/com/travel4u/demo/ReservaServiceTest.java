package com.travel4u.demo;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import com.travel4u.demo.servicio.model.Servicio;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
public class ReservaServiceTest {

    @Autowired
    private IReservaDAO reservaDAO;

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private IServicioDAO servicioDAO;

    @Test
    public void testCreateReserva() {
        // Buscar usuario existente
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail("admin@travel4u.com");
        assertTrue(usuarioOpt.isPresent(), "Usuario admin debe existir");
        Usuario usuario = usuarioOpt.get();

        // Buscar servicio existente
        List<Servicio> servicios = servicioDAO.findByTipoServicioAndActivoTrue("VUELO");
        if (servicios.isEmpty()) {
            servicios = servicioDAO.findAll();
        }
        assertFalse(servicios.isEmpty(), "Debe haber al menos un servicio");

        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setEstado("Confirmada");
        reserva.setTotal(new BigDecimal("299.99"));
        reserva.setMoneda("PEN");
        reserva.setObservaciones("Reserva de prueba");
        reserva.setCreatedAt(LocalDateTime.now());
        reserva.setUpdatedAt(LocalDateTime.now());

        // Guardar
        Reserva savedReserva = reservaDAO.save(reserva);
        assertNotNull(savedReserva.getIdReserva());
        System.out.println("✓ Reserva creada con ID: " + savedReserva.getIdReserva());

        // Verificar que se puede recuperar
        Reserva foundReserva = reservaDAO.findById(savedReserva.getIdReserva()).orElse(null);
        assertNotNull(foundReserva);
        assertEquals("Confirmada", foundReserva.getEstado());
        assertEquals(new BigDecimal("299.99"), foundReserva.getTotal());
        System.out.println("✓ Reserva recuperada correctamente");
    }

    @Test
    public void testFindReservasByUsuario() {
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail("admin@travel4u.com");
        assertTrue(usuarioOpt.isPresent(), "Usuario admin debe existir");
        Usuario usuario = usuarioOpt.get();

        List<Reserva> reservas = reservaDAO.findByUsuario(usuario);
        System.out.println("✓ Reservas encontradas para usuario " + usuario.getEmail() + ": " + reservas.size());
        
        for (Reserva reserva : reservas) {
            System.out.println("  - ID: " + reserva.getIdReserva() + 
                             ", Estado: " + reserva.getEstado() + 
                             ", Total: " + reserva.getTotal());
        }
    }

    @Test
    public void testReservaDataIntegrity() {
        List<Reserva> todasReservas = reservaDAO.findAll();
        System.out.println("✓ Total de reservas en BD: " + todasReservas.size());

        for (Reserva reserva : todasReservas) {
            // Verificar integridad de datos
            assertNotNull(reserva.getIdReserva(), "ID reserva no debe ser null");
            assertNotNull(reserva.getUsuario(), "Usuario no debe ser null");
            assertNotNull(reserva.getEstado(), "Estado no debe ser null");
            assertNotNull(reserva.getTotal(), "Total no debe ser null");
            assertTrue(reserva.getTotal().compareTo(BigDecimal.ZERO) >= 0, "Total debe ser >= 0");
            
            System.out.println("  Reserva " + reserva.getIdReserva() + 
                             " - Usuario: " + reserva.getUsuario().getEmail() +
                             " - Estado: " + reserva.getEstado() +
                             " - Total: " + reserva.getTotal());
        }
    }
}