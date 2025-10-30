package com.travel4u.demo.reserva.controller;

import com.travel4u.demo.reserva.model.Equipaje;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.model.Reserva_Equipaje;
import com.travel4u.demo.reserva.repository.IEquipajeDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.reserva.repository.IReservaEquipajeDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping
public class ReservaController {

    // --- MEJORA: Inyección de dependencias por constructor ---
    // Es una práctica recomendada sobre @Autowired en campos.
    // Hace que las dependencias sean explícitas y la clase más fácil de probar.
    private final IReservaDAO reservaDAO;
    private final IUsuarioDAO usuarioDAO;
    private final IEquipajeDAO equipajeDAO;
    private final IReservaEquipajeDAO reservaEquipajeDAO;
    private final com.travel4u.demo.servicio.repository.IServicioDAO servicioDAO;

    public ReservaController(IReservaDAO reservaDAO, IUsuarioDAO usuarioDAO, IEquipajeDAO equipajeDAO, IReservaEquipajeDAO reservaEquipajeDAO, com.travel4u.demo.servicio.repository.IServicioDAO servicioDAO) {
        this.reservaDAO = reservaDAO;
        this.usuarioDAO = usuarioDAO;
        this.equipajeDAO = equipajeDAO;
        this.reservaEquipajeDAO = reservaEquipajeDAO;
        this.servicioDAO = servicioDAO;
    }

    /**
     * Muestra la página "Mis Reservas" con la lista de reservas del usuario autenticado.
     * URL estandarizada a /reservas para que coincida con la plantilla.
     */
    @GetMapping("/reservas")
    public String showMisReservasPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("[DEBUG] Iniciando carga de TODAS las reservas (modo prueba)...");
        
        try {
            // TEMPORAL: Mostrar todas las reservas sin filtrar por usuario
            List<Reserva> todasLasReservas = reservaDAO.findAll();
            System.out.println("[DEBUG] Total de reservas encontradas: " + todasLasReservas.size());
            
            for (int i = 0; i < todasLasReservas.size() && i < 5; i++) {
                Reserva r = todasLasReservas.get(i);
                System.out.println("[DEBUG] Reserva " + (i+1) + ": ID=" + r.getIdReserva() + 
                                 ", Estado=" + r.getEstado() + 
                                 ", Total=" + r.getTotal() + 
                                 ", Usuario=" + (r.getUsuario() != null ? r.getUsuario().getEmail() : "null"));
            }

            model.addAttribute("reservas", todasLasReservas);
            System.out.println("[DEBUG] Modelo con todas las reservas configurado correctamente");
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar todas las reservas: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("reservas", java.util.Collections.emptyList());
        }
        
        return "reservas";
    }

    /**
     * Muestra la página de detalles de una reserva específica.
     * URL estandarizada a /reservas/{id}
     */
    @GetMapping("/reservas/{id}")
    public String showReservaDetallePage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("[DEBUG] Cargando detalle de reserva ID: " + id);
        
        try {
            // TEMPORAL: Sin validación de usuario para pruebas
            Reserva reserva = reservaDAO.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada con ID: " + id));

            System.out.println("[DEBUG] Reserva encontrada: ID=" + reserva.getIdReserva() + 
                             ", Usuario=" + (reserva.getUsuario() != null ? reserva.getUsuario().getEmail() : "null"));

            // Cargamos los datos adicionales que la vista de detalle necesita.
            List<Reserva_Equipaje> equipajesDeLaReserva = reservaEquipajeDAO.findByReserva(reserva);
            System.out.println("[DEBUG] Equipajes encontrados: " + equipajesDeLaReserva.size());

            model.addAttribute("reserva", reserva);
            model.addAttribute("equipajes", equipajesDeLaReserva);

            return "reserva-detalle";
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar detalle de reserva: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Muestra la página de selección de asientos para iniciar una nueva reserva.
     * (Mantenido de tu versión original)
     */
    @GetMapping("/reservar/servicio/{idServicio}")
    public String showAsientosPageForService(@PathVariable("idServicio") Long idServicio, Model model) {
        System.out.println("[DEBUG] Iniciando reserva para servicio ID: " + idServicio);
        
        try {
            com.travel4u.demo.servicio.repository.IServicioDAO servicioDAO = this.servicioDAO;
            com.travel4u.demo.servicio.model.Servicio servicio = servicioDAO.findById(idServicio)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
            
            model.addAttribute("servicio", servicio);
            model.addAttribute("reserva", new Reserva());
            
            List<Equipaje> tiposDeEquipaje = equipajeDAO.findAll();
            model.addAttribute("tiposDeEquipaje", tiposDeEquipaje);
            
            return "asientos";
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar servicio: " + e.getMessage());
            throw e;
        }
    }
    
    @GetMapping("/reservar/vuelo/{idVuelo}")
    public String showAsientosPage(@PathVariable("idVuelo") Long idVuelo, Model model) {
        return "redirect:/reservar/servicio/" + idVuelo;
    }


    // En ReservaController.java, añade este método junto a los otros

    /**
     * NUEVO: Endpoint de API para obtener los detalles de una reserva como JSON.
     * Esto será consumido por JavaScript para llenar el popup.
     */
    @GetMapping("/api/reservas/{id}")
    @ResponseBody // <-- ¡Muy importante! Indica que la respuesta es JSON, no una vista.
    public Reserva getReservaDetalleJson(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        Reserva reserva = reservaDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

        // Verificación de seguridad
        if (!reserva.getUsuario().getEmail().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        return reserva;
    }


}