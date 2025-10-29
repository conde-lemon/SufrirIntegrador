package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.scraper.model.Oferta;
import com.travel4u.demo.scraper.service.ScrapingService;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
public class AppController {

    @Autowired(required = false)
    private IReservaDAO reservaDAO;

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScrapingService scrapingService;

    /**
     * Muestra la página de inicio.
     * AHORA: Obtiene las ofertas Y las reservas del usuario (si está logueado).
     */
    @GetMapping("/")
    public String viewHomePage(Model model, Principal principal) { // <-- CAMBIO 1: Añadir Principal
        // 1. Obtener las ofertas (esto ya estaba bien)
        List<Oferta> ofertas = scrapingService.scrapeOfertasPrincipales();
        model.addAttribute("ofertas", ofertas);

        // ¡CAMBIO 2: Obtener las reservas del usuario!
        List<Reserva> reservas;
        if (principal != null) {
            // Si el usuario está logueado, buscamos sus reservas
            reservas = usuarioDAO.findByEmail(principal.getName())
                    .map(reservaDAO::findByUsuario)
                    .orElse(Collections.emptyList());
        } else {
            // Si no está logueado, la lista de reservas está vacía
            reservas = Collections.emptyList();
        }

        // ¡CAMBIO 3: Añadir la lista de reservas al modelo!
        model.addAttribute("reservas", reservas);

        return "index";
    }

    /**
     * Muestra la página de login.
     */
    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    /**
     * Muestra el formulario de registro.
     */
    @GetMapping("/registrar")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }

    /**
     * Procesa los datos del formulario de registro.
     * Usa el PasswordEncoder configurado (actualmente NoOpPasswordEncoder).
     */
    @PostMapping("/registrar")
    public String processRegistration(Usuario usuario, RedirectAttributes redirectAttributes) {
        // Verificar si el email ya existe
        if (usuarioDAO.findByEmail(usuario.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado.");
            return "redirect:/registrar";
        }

        // Se usa el PasswordEncoder que esté configurado (NoOpPasswordEncoder en tu caso)
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("USUARIO");
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        usuarioDAO.save(usuario);

        redirectAttributes.addFlashAttribute("success", "¡Registro exitoso! Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }

    /**
     * Muestra el perfil del usuario autenticado.
     */
    @GetMapping("/perfil")
    public String showProfilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Error de seguridad: Usuario autenticado no encontrado en la BD."));

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    /**
     * Muestra la página de resultados de búsqueda de vuelos.
     */
    @GetMapping("/vuelos/buscar")
    public String showFlightResults(
            @RequestParam("origen") String origen,
            @RequestParam("destino") String destino,
            Model model) {

        model.addAttribute("origenBusqueda", origen);
        model.addAttribute("destinoBusqueda", destino);

        // Aquí iría la lógica para buscar vuelos en la base de datos o API externa.
        // List<Vuelo> vuelosEncontrados = vueloService.buscarVuelos(origen, destino);
        // model.addAttribute("vuelos", vuelosEncontrados);

        return "resultados-vuelos";
    }

    // --- Métodos para el resto de páginas de navegación ---

    @GetMapping("/vuelos")
    public String showVuelosPage() {
        return "vuelos";
    }

    @GetMapping("/cruceros")
    public String showCrucerosPage() {
        return "cruceros";
    }

    @GetMapping("/bus")
    public String showBusPage() {
        return "bus";
    }

    @GetMapping("/hospedaje")
    public String showHospedajePage() {
        return "hospedaje";
    }

    @GetMapping("/paquetes-y-promociones")
    public String showOfertasPage() {
        return "ofertas";
    }

    @GetMapping("/terminos-y-condiciones")
    public String showTerminosPage() {
        return "terminos_y_condiciones";
    }

    @GetMapping("/confirmacion-reserva")
    public String showConfirmacionReservaPage(
            @RequestParam(required = false) Long idReserva,
            Model model,
            Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado para confirmación de reserva."));

        // Sugerencia: El código de reserva debería generarse y guardarse con la Reserva.
        String codigoReserva = "TFU-PENDIENTE"; // Valor por defecto más claro
        if (idReserva != null) {
            // Idealmente, buscar la reserva por idReserva y obtener su código de reserva guardado.
            // Por ahora, mantenemos la lógica de generación si no hay un campo en Reserva.
            codigoReserva = String.format("TFU-%d-%04d",
                    java.time.LocalDate.now().getYear(),
                    idReserva);
        }

        model.addAttribute("idUsuario", usuario.getIdUsuario());
        model.addAttribute("codigoReserva", codigoReserva);

        return "confirmacion-reserva";
    }
}