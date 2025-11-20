package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Pago;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IPagoDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.reserva.service.PagoService;
import com.travel4u.demo.service.EmailService;
import com.travel4u.demo.service.NotificationService;
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
    private final PagoService pagoService;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public PagoController(IPagoDAO pagoDAO, IReservaDAO reservaDAO, IUsuarioDAO usuarioDAO, IServicioDAO servicioDAO, PagoService pagoService, EmailService emailService, NotificationService notificationService) {
        this.pagoDAO = pagoDAO;
        this.reservaDAO = reservaDAO;
        this.usuarioDAO = usuarioDAO;
        this.servicioDAO = servicioDAO;
        this.pagoService = pagoService;
        this.emailService = emailService;
        this.notificationService = notificationService;
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





    @GetMapping("/pago/paypal")
    public String mostrarPasarelaPayPal(
            @RequestParam Integer idReserva,
            Model model,
            Principal principal) {
        
        if (principal == null) {
            return "redirect:/login";
        }

        Reserva reserva = reservaDAO.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!reserva.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return "redirect:/reservas";
        }

        model.addAttribute("reserva", reserva);
        model.addAttribute("emailUsuario", usuario.getEmail());
        return "pasarela-paypal";
    }

    @PostMapping("/pago/paypal/procesar")
    public String procesarPagoPayPal(
            @RequestParam Integer idReserva,
            @RequestParam String emailPayPal,
            @RequestParam String passwordPayPal,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Reserva reserva = reservaDAO.findById(idReserva)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!reserva.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new RuntimeException("Acceso denegado");
            }

            Pago pago = pagoService.procesarPagoPayPal(reserva, emailPayPal);

            // Enviar email de confirmación
            emailService.enviarConfirmacionPago(reserva, pago);
            emailService.enviarBoletaElectronica(reserva);
            
            // Programar notificaciones
            notificationService.enviarNotificacionReserva(reserva, "CONFIRMACION");
            notificationService.programarRecordatorioViaje(reserva);

            redirectAttributes.addFlashAttribute("pagoExitoso", true);
            redirectAttributes.addFlashAttribute("referenciaPago", pago.getReferenciaPago());
            redirectAttributes.addFlashAttribute("montoPagado", pago.getMonto());
            
            return "redirect:/pago/confirmacion?idReserva=" + idReserva;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/pago/paypal?idReserva=" + idReserva;
        }
    }

    @GetMapping("/pago/procesar/{idReserva}")
    public String mostrarPagoProcesar(
            @PathVariable Integer idReserva,
            Model model,
            Principal principal) {
        
        System.out.println("[DEBUG] Accediendo a /pago/procesar/" + idReserva);
        
        if (principal == null) {
            System.out.println("[DEBUG] Usuario no autenticado, redirigiendo a login");
            return "redirect:/login";
        }
        
        try {
            Reserva reserva = reservaDAO.findById(idReserva)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
            
            Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Verificar que la reserva pertenece al usuario
            if (!reserva.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                return "redirect:/reservas";
            }
            
            System.out.println("[DEBUG] Reserva encontrada: ID=" + reserva.getIdReserva() + ", Total=" + reserva.getTotal());
            System.out.println("[DEBUG] Mostrando página de pago para usuario: " + usuario.getEmail());
            
            model.addAttribute("reserva", reserva);
            model.addAttribute("emailUsuario", usuario.getEmail());
            
            return "pago";
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al mostrar página de pago: " + e.getMessage());
            return "redirect:/reservas";
        }
    }

    @GetMapping("/pago/confirmacion")
    public String mostrarConfirmacionPago(
            @RequestParam Integer idReserva,
            Model model,
            Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Reserva reserva = reservaDAO.findById(idReserva)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar que la reserva pertenece al usuario
            if (!reserva.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                return "redirect:/reservas";
            }

            // Buscar el pago asociado a la reserva
            Pago pago = pagoDAO.findByReserva(reserva).orElse(null);
            
            model.addAttribute("reserva", reserva);
            if (pago != null) {
                model.addAttribute("referenciaPago", pago.getReferenciaPago());
                model.addAttribute("metodoPago", pago.getMetodoPago());
            }
            
            return "confirmacion-pago";
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al mostrar confirmación: " + e.getMessage());
            return "redirect:/reservas";
        }
    }
}