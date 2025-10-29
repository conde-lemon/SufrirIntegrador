package com.travel4u.demo.reserva.controller;

import com.travel4u.demo.reserva.model.Equipaje;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.model.Reserva_Equipaje;
import com.travel4u.demo.reserva.repository.IEquipajeDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.reserva.repository.IReservaEquipajeDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Importar HttpStatus
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException; // Importar ResponseStatusException
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReservaController {

    @Autowired
    private IReservaDAO reservaDAO;

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private IEquipajeDAO equipajeDAO;

    @Autowired
    private IReservaEquipajeDAO reservaEquipajeDAO;

    /**
     * Muestra la página "Mis Reservas" con la lista de reservas del usuario autenticado.
     * URL CAMBIADA para evitar conflicto con AppController.
     */
    @GetMapping("/mis-reservas") // <-- CAMBIO DE URL
    public String showMisReservasPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario autenticado no encontrado o sesión inválida."));

        List<Reserva> misReservas = reservaDAO.findByUsuario(usuario);

        model.addAttribute("reservas", misReservas);
        return "reservas"; // Asume que la plantilla se llama 'reservas.html'
    }

    /**
     * Muestra la página de selección de asientos para iniciar una nueva reserva.
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
     */
    @PostMapping("/reservar/crear")
    public String crearReserva(
            Reserva reserva, // Considerar usar un DTO para la entrada del formulario
            @RequestParam(name = "equipajeIds", required = false) List<Integer> equipajeIds,
            Principal principal,
            RedirectAttributes attributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario no encontrado para crear la reserva."));

        // SUGERENCIA: El precio base del vuelo debería obtenerse de un servicio de vuelos, no hardcodeado.
        BigDecimal totalReserva = new BigDecimal("350.00"); // Precio base del vuelo (ejemplo)
        reserva.setUsuario(usuario);
        reserva.setEstado("Pendiente"); // Considerar usar un enum para estados
        reserva.setFechaInicio(LocalDateTime.of(2025, 10, 15, 9, 30)); // Debería venir del formulario o vuelo
        reserva.setMoneda("PEN"); // Considerar usar un enum o constante

        reserva.setTotal(totalReserva);
        Reserva reservaGuardada = reservaDAO.save(reserva);

        if (equipajeIds != null && !equipajeIds.isEmpty()) {
            for (Integer equipajeId : equipajeIds) {
                Equipaje equipaje = equipajeDAO.findById(equipajeId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de equipaje no válido."));

                Reserva_Equipaje reservaEquipaje = new Reserva_Equipaje();
                reservaEquipaje.setReserva(reservaGuardada);
                reservaEquipaje.setEquipaje(equipaje);
                reservaEquipaje.setCantidad(1); // SUGERENCIA: Si la UI permite, esto debería ser variable
                reservaEquipaje.setPrecioUnitario(equipaje.getPrecio());
                reservaEquipaje.setSubtotal(equipaje.getPrecio());

                reservaEquipajeDAO.save(reservaEquipaje);

                totalReserva = totalReserva.add(equipaje.getPrecio());
            }
        }

        reservaGuardada.setTotal(totalReserva);
        reservaDAO.save(reservaGuardada);

        attributes.addFlashAttribute("success", "¡Tu reserva ha sido creada con éxito!");
        return "redirect:/mis-reservas"; // Redirigir a la nueva URL
    }

    /**
     * Muestra la página de detalles de una reserva específica.
     * Incluye una validación de seguridad para asegurar que el usuario solo vea sus propias reservas.
     */
    @GetMapping("/mis-reservas/{id}") // <-- URL ACTUALIZADA
    public String showReservaDetallePage(@PathVariable("id") Long id, Model model, Principal principal) { // <-- ID a Long
        if (principal == null) {
            return "redirect:/login";
        }

        Reserva reserva = reservaDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada con ID: " + id));

        if (!reserva.getUsuario().getEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado. No tienes permiso para ver esta reserva.");
        }

        List<Reserva_Equipaje> equipajesDeLaReserva = reservaEquipajeDAO.findByReserva(reserva);

        model.addAttribute("reserva", reserva);
        model.addAttribute("equipajes", equipajesDeLaReserva);

        return "reserva-detalle";
    }
}