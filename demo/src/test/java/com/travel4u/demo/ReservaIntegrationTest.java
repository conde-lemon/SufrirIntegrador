package com.travel4u.demo;

import com.travel4u.demo.reserva.model.Detalle_Reserva;
import com.travel4u.demo.reserva.model.Equipaje;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.model.Reserva_Equipaje;
import com.travel4u.demo.reserva.repository.IDetalleReservaDAO;
import com.travel4u.demo.reserva.repository.IEquipajeDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.reserva.repository.IReservaEquipajeDAO;
import com.travel4u.demo.servicio.model.Servicio;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
public class ReservaIntegrationTest {

    @Autowired
    private IReservaDAO reservaDAO;
    
    @Autowired
    private IDetalleReservaDAO detalleReservaDAO;
    
    @Autowired
    private IUsuarioDAO usuarioDAO;
    
    @Autowired
    private IServicioDAO servicioDAO;
    
    @Autowired
    private IEquipajeDAO equipajeDAO;
    
    @Autowired
    private IReservaEquipajeDAO reservaEquipajeDAO;

    @Test
    public void testCrearReservaCompleta() {
        // Crear usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setEmail("test@travel4u.com");
        usuario.setPassword("test123");
        usuario.setRol("USER");
        usuario = usuarioDAO.save(usuario);

        // Crear servicio de prueba
        Servicio servicio = new Servicio();
        servicio.setTipoServicio("VUELO");
        servicio.setNombre("Vuelo Lima - Madrid");
        servicio.setOrigen("Lima");
        servicio.setDestino("Madrid");
        servicio.setPrecioBase(new BigDecimal("850.00"));
        servicio.setDisponibilidad(100);
        servicio.setDescripcion("Vuelo directo");
        servicio = servicioDAO.save(servicio);

        // Crear equipaje de prueba
        Equipaje equipaje = new Equipaje();
        equipaje.setTipo("Equipaje de mano");
        equipaje.setPrecio(new BigDecimal("50.00"));
        equipaje.setPesoMax(new BigDecimal("10.0"));
        equipaje.setDimensionLargo(new BigDecimal("55.0"));
        equipaje.setDimensionAncho(new BigDecimal("40.0"));
        equipaje.setDimensionAlto(new BigDecimal("20.0"));
        equipaje = equipajeDAO.save(equipaje);

        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setEstado("CONFIRMADA");
        reserva.setMoneda("PEN");
        reserva.setTotal(new BigDecimal("900.00"));
        reserva = reservaDAO.save(reserva);

        // Crear detalle de reserva con datos del pasajero
        Detalle_Reserva detalleReserva = new Detalle_Reserva();
        detalleReserva.setReserva(reserva);
        detalleReserva.setServicio(servicio);
        detalleReserva.setCantidad(1);
        detalleReserva.setPrecioUnitario(servicio.getPrecioBase());
        detalleReserva.setSubtotal(servicio.getPrecioBase());
        
        // Datos del pasajero
        detalleReserva.setNombre("Juan");
        detalleReserva.setApellido("Pérez");
        detalleReserva.setDocumento("12345678");
        detalleReserva.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        detalleReserva.setSexo("MASCULINO");
        detalleReserva.setCorreo("juan.perez@email.com");
        detalleReserva.setTelefono("987654321");
        detalleReserva.setAsientoSeleccionado("12A");
        
        detalleReserva = detalleReservaDAO.save(detalleReserva);

        // Crear relación con equipaje
        Reserva_Equipaje reservaEquipaje = new Reserva_Equipaje();
        reservaEquipaje.setReserva(reserva);
        reservaEquipaje.setEquipaje(equipaje);
        reservaEquipaje.setCantidad(1);
        reservaEquipaje.setPrecioUnitario(equipaje.getPrecio());
        reservaEquipaje.setSubtotal(equipaje.getPrecio());
        reservaEquipajeDAO.save(reservaEquipaje);

        // Verificaciones
        assertNotNull(reserva.getIdReserva());
        assertEquals("CONFIRMADA", reserva.getEstado());
        assertEquals(new BigDecimal("900.00"), reserva.getTotal());
        
        assertNotNull(detalleReserva.getIdDetalleReserva());
        assertEquals("Juan", detalleReserva.getNombre());
        assertEquals("Pérez", detalleReserva.getApellido());
        assertEquals("12345678", detalleReserva.getDocumento());
        assertEquals("12A", detalleReserva.getAsientoSeleccionado());
        assertEquals("juan.perez@email.com", detalleReserva.getCorreo());
        
        System.out.println("✅ Test completado exitosamente");
        System.out.println("Reserva ID: " + reserva.getIdReserva());
        System.out.println("Pasajero: " + detalleReserva.getNombre() + " " + detalleReserva.getApellido());
        System.out.println("Asiento: " + detalleReserva.getAsientoSeleccionado());
        System.out.println("Total: S/ " + reserva.getTotal());
    }
}