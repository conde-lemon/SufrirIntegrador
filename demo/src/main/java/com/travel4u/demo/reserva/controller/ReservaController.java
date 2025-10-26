package com.travel4u.demo.reserva.controller;

import com.travel4u.demo.reserva.model.Equipaje;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.model.Reserva_Equipaje; // ¡IMPORTACIÓN AÑADIDA!
import com.travel4u.demo.reserva.repository.IEquipajeDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.reserva.repository.IReservaEquipajeDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReservaController {

    // --- Inyección de dependencias agrupada para mayor claridad ---
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
     */
    @GetMapping("/reservas")
    public String showMisReservasPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // CORRECCIÓN: Usar orElseThrow para manejar de forma segura si el usuario no se encuentra.
        Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado."));

        List<Reserva> misReservas = reservaDAO.findByUsuario(usuario);

        model.addAttribute("reservas", misReservas);
        return "reservas";
    }

    /**
     * Muestra la página de selección de asientos para iniciar una nueva reserva.
     */
    @GetMapping("/reservar/vuelo/{idVuelo}")
    public String showAsientosPage(@PathVariable("idVuelo") Long idVuelo, Model model) {
        // Simulamos los datos del vuelo
        model.addAttribute("idVuelo", idVuelo);
        model.addAttribute("origen", "Lima");
        model.addAttribute("destino", "Buenos Aires");
        model.addAttribute("fecha", "15 Octubre 2025");

        // Pasamos un objeto Reserva vacío para el binding del formulario
        model.addAttribute("reserva", new Reserva());

        // Obtenemos todos los tipos de equipaje y los pasamos a la vista
        List<Equipaje> tiposDeEquipaje = equipajeDAO.findAll();
        model.addAttribute("tiposDeEquipaje", tiposDeEquipaje);

        return "asientos";
    }

    /**
     * Procesa el formulario completo para crear la reserva y su equipaje asociado.
     */
    @PostMapping("/reservar/crear")
    public String crearReserva(
            Reserva reserva,
            @RequestParam(name = "equipajeIds", required = false) List<Integer> equipajeIds,
            Principal principal,
            RedirectAttributes attributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioDAO.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado para crear la reserva."));

        // 1. Configurar y calcular el total inicial de la reserva (precio base del vuelo)
        BigDecimal totalReserva = new BigDecimal("350.00"); // Precio base del vuelo (ejemplo)
        reserva.setUsuario(usuario);
        reserva.setEstado("Pendiente");
        reserva.setFechaInicio(LocalDateTime.of(2025, 10, 15, 9, 30));
        reserva.setMoneda("PEN");

        // 2. Guardar la reserva principal PRIMERO para obtener su ID
        reserva.setTotal(totalReserva); // Asignamos el total parcial
        Reserva reservaGuardada = reservaDAO.save(reserva);

        // 3. Procesar el equipaje seleccionado
        if (equipajeIds != null && !equipajeIds.isEmpty()) {
            for (Integer equipajeId : equipajeIds) {
                Equipaje equipaje = equipajeDAO.findById(equipajeId)
                        .orElseThrow(() -> new IllegalStateException("Tipo de equipaje no válido."));

                // Crear la entidad de relación Reserva_Equipaje
                Reserva_Equipaje reservaEquipaje = new Reserva_Equipaje(); // Ahora sí la encuentra
                reservaEquipaje.setReserva(reservaGuardada);
                reservaEquipaje.setEquipaje(equipaje);
                reservaEquipaje.setCantidad(1); // Asumimos 1 por cada tipo seleccionado
                reservaEquipaje.setPrecioUnitario(equipaje.getPrecio());
                reservaEquipaje.setSubtotal(equipaje.getPrecio()); // Subtotal es precio * cantidad

                // Guardar la relación
                reservaEquipajeDAO.save(reservaEquipaje);

                // Añadir el costo del equipaje al total de la reserva
                totalReserva = totalReserva.add(equipaje.getPrecio());
            }
        }

        // 4. Actualizar la reserva con el total final (vuelo + equipaje)
        reservaGuardada.setTotal(totalReserva);
        reservaDAO.save(reservaGuardada);

        attributes.addFlashAttribute("success", "¡Tu reserva ha sido creada con éxito!");
        return "redirect:/reservas";
    }
    // ... (importaciones existentes)

    /**
     * NUEVO: Muestra la página de detalles de una reserva específica.
     * Incluye una validación de seguridad para asegurar que el usuario solo vea sus propias reservas.
     */
    @GetMapping("/reservas/{id}")
    public String showReservaDetallePage(@PathVariable("id") Integer id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // 1. Buscar la reserva por su ID
        Reserva reserva = reservaDAO.findById(id)
                .orElseThrow(() -> new IllegalStateException("Reserva no encontrada con ID: " + id));

        // 2. ¡VALIDACIÓN DE SEGURIDAD CRUCIAL!
        // Verificamos que el email del dueño de la reserva coincida con el del usuario logueado.
        if (!reserva.getUsuario().getEmail().equals(principal.getName())) {
            // Si no coincide, es un intento de acceso no autorizado.
            throw new IllegalStateException("Acceso denegado. No tienes permiso para ver esta reserva.");
        }

        // 3. Buscar los equipajes asociados a esta reserva
        List<Reserva_Equipaje> equipajesDeLaReserva = reservaEquipajeDAO.findByReserva(reserva);

        // 4. Pasar todos los datos a la vista
        model.addAttribute("reserva", reserva);
        model.addAttribute("equipajes", equipajesDeLaReserva);

        return "reserva-detalle"; // Renderiza la nueva plantilla 'reserva-detalle.html'
    }

// ... (resto de los métodos del controlador)
}