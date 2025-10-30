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
public class ReservaController {

    // --- MEJORA: Inyección de dependencias por constructor ---
    // Es una práctica recomendada sobre @Autowired en campos.
    // Hace que las dependencias sean explícitas y la clase más fácil de probar.
    private final IReservaDAO reservaDAO;
    private final IUsuarioDAO usuarioDAO;
    private final IEquipajeDAO equipajeDAO;
    private final IReservaEquipajeDAO reservaEquipajeDAO;

    public ReservaController(IReservaDAO reservaDAO, IUsuarioDAO usuarioDAO, IEquipajeDAO equipajeDAO, IReservaEquipajeDAO reservaEquipajeDAO) {
        this.reservaDAO = reservaDAO;
        this.usuarioDAO = usuarioDAO;
        this.equipajeDAO = equipajeDAO;
        this.reservaEquipajeDAO = reservaEquipajeDAO;
    }

    /**
     * Muestra la página "Mis Reservas" con la lista de reservas del usuario autenticado.
     * URL estandarizada a /reservas para que coincida con la plantilla.
     */
    @GetMapping("/reservas")
    public String showMisReservasPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no encontrado."));

        // CORRECCIÓN: Usamos el método que ordena por fecha para una mejor experiencia de usuario.
        List<Reserva> misReservas = reservaDAO.findByUsuarioOrderByCreatedAtDesc(usuario);

        model.addAttribute("reservas", misReservas);
        return "reservas";
    }

    /**
     * Muestra la página de detalles de una reserva específica.
     * URL estandarizada a /reservas/{id}
     */
    @GetMapping("/reservas/{id}")
    public String showReservaDetallePage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        // CORRECCIÓN: El ID de la reserva es Integer, no Long.
        Reserva reserva = reservaDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada con ID: " + id));

        // VERIFICACIÓN DE SEGURIDAD: El usuario solo puede ver sus propias reservas.
        if (!reserva.getUsuario().getEmail().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado. No tienes permiso para ver esta reserva.");
        }

        // Cargamos los datos adicionales que la vista de detalle necesita.
        List<Reserva_Equipaje> equipajesDeLaReserva = reservaEquipajeDAO.findByReserva(reserva);

        model.addAttribute("reserva", reserva);
        model.addAttribute("equipajes", equipajesDeLaReserva);

        return "reserva-detalle";
    }

    /**
     * Muestra la página de selección de asientos para iniciar una nueva reserva.
     * (Mantenido de tu versión original)
     */
    @GetMapping("/reservar/vuelo/{idVuelo}")
    public String showAsientosPage(@PathVariable("idVuelo") Long idVuelo, Model model) {
        // SUGERENCIA: Estos datos deberían venir de un servicio de vuelos, no hardcodeados.
        model.addAttribute("idVuelo", idVuelo);
        model.addAttribute("origen", "Lima");
        model.addAttribute("destino", "Buenos Aires");
        model.addAttribute("fecha", "15 Octubre 2025");

        model.addAttribute("reserva", new Reserva());

        List<Equipaje> tiposDeEquipaje = equipajeDAO.findAll();
        model.addAttribute("tiposDeEquipaje", tiposDeEquipaje);

        return "asientos";
    }

    /**
     * Procesa el formulario completo para crear la reserva y su equipaje asociado.
     * (Mantenido de tu versión original)
     */
    @PostMapping("/reservar/crear")
    public String crearReserva(
            Reserva reserva,
            @RequestParam(name = "equipajeIds", required = false) List<Integer> equipajeIds,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes attributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no encontrado para crear la reserva."));

        // SUGERENCIA: El precio base del vuelo debería obtenerse de un servicio de vuelos, no hardcodeado.
        BigDecimal totalReserva = new BigDecimal("350.00"); // Precio base del vuelo (ejemplo)
        reserva.setUsuario(usuario);
        reserva.setEstado("Pendiente");
        reserva.setFechaInicio(LocalDateTime.of(2025, 10, 15, 9, 30));
        reserva.setMoneda("PEN");

        reserva.setTotal(totalReserva);
        Reserva reservaGuardada = reservaDAO.save(reserva);

        if (equipajeIds != null && !equipajeIds.isEmpty()) {
            for (Integer equipajeId : equipajeIds) {
                Equipaje equipaje = equipajeDAO.findById(equipajeId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de equipaje no válido."));

                Reserva_Equipaje reservaEquipaje = new Reserva_Equipaje();
                reservaEquipaje.setReserva(reservaGuardada);
                reservaEquipaje.setEquipaje(equipaje);
                reservaEquipaje.setCantidad(1);
                reservaEquipaje.setPrecioUnitario(equipaje.getPrecio());
                reservaEquipaje.setSubtotal(equipaje.getPrecio());

                reservaEquipajeDAO.save(reservaEquipaje);

                totalReserva = totalReserva.add(equipaje.getPrecio());
            }
        }

        reservaGuardada.setTotal(totalReserva);
        reservaDAO.save(reservaGuardada);

        attributes.addFlashAttribute("success", "¡Tu reserva ha sido creada con éxito!");
        return "redirect:/reservas"; // Redirigir a la URL estandarizada
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