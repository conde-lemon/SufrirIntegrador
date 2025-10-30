package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Pago;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IPagoDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.servicio.model.Servicio;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class PagoController {

    private final IPagoDAO pagoDAO;
    private final IReservaDAO reservaDAO;
    private final IUsuarioDAO usuarioDAO;
    private final IServicioDAO servicioDAO;

    public PagoController(IPagoDAO pagoDAO, IReservaDAO reservaDAO, IUsuarioDAO usuarioDAO, IServicioDAO servicioDAO) {
        this.pagoDAO = pagoDAO;
        this.reservaDAO = reservaDAO;
        this.usuarioDAO = usuarioDAO;
        this.servicioDAO = servicioDAO;
    }

    @GetMapping("/pago")
    public String showPagoPage(
            @RequestParam(required = false) Long idServicio,
            @RequestParam(required = false) String asientoSeleccionado,
            @RequestParam(required = false) String equipajeIds,
            Model model, 
            Principal principal) {
        
        System.out.println("[DEBUG] GET /pago recibido en PagoController");
        
        if (principal == null) {
            return "redirect:/login";
        }
        
        if (idServicio != null) {
            Servicio servicio = servicioDAO.findById(idServicio).orElse(null);
            model.addAttribute("servicio", servicio);
        }
        
        model.addAttribute("reserva", new Reserva());
        return "pago-simple";
    }
    
    @PostMapping("/pago/test")
    public String testPost() {
        System.out.println("[DEBUG] POST /pago/test funciona!");
        return "redirect:/pago";
    }

    @GetMapping("/reservar/crear")
    public String crearReservaDirecta(
            @RequestParam(name = "idServicio", required = false) Long idServicio,
            @RequestParam(name = "asientoSeleccionado", required = false) String asientoSeleccionado,
            @RequestParam(name = "equipajeIds", required = false) String equipajeIds,
            @RequestParam(name = "observaciones", required = false) String observaciones,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        System.out.println("[DEBUG] GET /reservar/crear - Creación directa de reserva");
        System.out.println("[DEBUG] Parámetros: idServicio=" + idServicio + ", asiento=" + asientoSeleccionado + ", observaciones=" + observaciones);
        
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Reserva reserva = new Reserva();
            BigDecimal total = new BigDecimal("500.00"); // Precio temporal
            
            if (idServicio != null) {
                Servicio servicio = servicioDAO.findById(idServicio).orElse(null);
                if (servicio != null) {
                    total = servicio.getPrecioBase().add(new BigDecimal("45.00"));
                }
            }

            // Crear reserva temporal
            reserva.setUsuario(usuario);
            reserva.setEstado("Confirmada");
            reserva.setFechaInicio(LocalDateTime.now().plusDays(30));
            reserva.setMoneda("PEN");
            reserva.setTotal(total);
            String obsCompletas = "Asiento: " + (asientoSeleccionado != null ? asientoSeleccionado : "No especificado");
            if (observaciones != null && !observaciones.trim().isEmpty()) {
                obsCompletas += ", Pasajero: " + observaciones.trim();
            }
            if (equipajeIds != null && !equipajeIds.trim().isEmpty()) {
                obsCompletas += ", Equipaje: " + equipajeIds;
            }
            reserva.setObservaciones(obsCompletas);

            Reserva reservaGuardada = reservaDAO.save(reserva);
            System.out.println("[DEBUG] Reserva creada con ID: " + reservaGuardada.getIdReserva());

            // Crear pago automático
            try {
                Pago pago = new Pago();
                pago.setReserva(reservaGuardada);
                pago.setMonto(total);
                pago.setMetodoPago("Procesado automáticamente");
                pago.setEstadoPago("Completado");
                pago.setFechaPago(LocalDateTime.now());
                pago.setReferenciaPago("AUTO-" + System.currentTimeMillis());
                pagoDAO.save(pago);
                System.out.println("[DEBUG] Pago automático creado");
            } catch (Exception e) {
                System.err.println("[WARN] Error al crear pago automático: " + e.getMessage());
            }

            return "redirect:/confirmacion-reserva?idReserva=" + reservaGuardada.getIdReserva();
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al crear reserva: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/confirmacion-reserva";
        }
    }

    @PostMapping("/reservar/crear")
    public String procesarPago(
            @RequestParam(name = "idServicio", required = false) Long idServicio,
            @RequestParam(name = "asientoSeleccionado", required = false) String asientoSeleccionado,
            @RequestParam(name = "equipajeIds", required = false) String equipajeIds,
            @RequestParam(name = "metodoPago", required = false) String metodoPago,
            Reserva reserva,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        System.out.println("[DEBUG] POST /reservar/crear recibido en PagoController");
        System.out.println("[DEBUG] idServicio: " + idServicio + ", asiento: " + asientoSeleccionado);
        
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Servicio servicio = null;
            BigDecimal total = new BigDecimal("0.00");
            
            if (idServicio != null) {
                servicio = servicioDAO.findById(idServicio)
                        .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
                total = servicio.getPrecioBase().add(new BigDecimal("45.00")); // Tasas e impuestos
            }

            // Crear reserva
            reserva.setUsuario(usuario);
            reserva.setEstado("Confirmada");
            reserva.setFechaInicio(LocalDateTime.now().plusDays(30));
            reserva.setMoneda("PEN");
            reserva.setTotal(total);
            
            if (asientoSeleccionado != null) {
                reserva.setObservaciones("Asiento: " + asientoSeleccionado);
            }

            Reserva reservaGuardada = reservaDAO.save(reserva);

            // Crear pago
            Pago pago = new Pago();
            pago.setReserva(reservaGuardada);
            pago.setMonto(total);
            pago.setMetodoPago(metodoPago != null ? metodoPago : "tarjeta");
            pago.setEstadoPago("Completado");
            pago.setFechaPago(LocalDateTime.now());
            pago.setReferenciaPago("PAY-" + System.currentTimeMillis());

            pagoDAO.save(pago);

            redirectAttributes.addFlashAttribute("success", "¡Pago procesado exitosamente!");
            return "redirect:/confirmacion-reserva?idReserva=" + reservaGuardada.getIdReserva();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/pago";
        }
    }
}