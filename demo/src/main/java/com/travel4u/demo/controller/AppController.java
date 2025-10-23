package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.scraper.model.Oferta;
import com.travel4u.demo.scraper.service.FlightService;
import com.travel4u.demo.scraper.service.ScrapingService;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
public class AppController {

    // CORRECCIÓN: Se añade el logger que faltaba
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private IReservaDAO reservaDAO;

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CORRECCIÓN: Se elimina la inyección duplicada
    @Autowired
    private ScrapingService scrapingService;

    @Autowired
    private FlightService flightService;

    /**
     * Muestra la página de inicio con ofertas de la API de Amadeus.
     * Si la API falla, usa el web scraper como respaldo.
     */
    @GetMapping("/")
    public String viewHomePage(Model model, Principal principal) {
        // 1. Intentamos obtener ofertas REALES desde la API de Amadeus.
        List<Oferta> ofertasDestacadas = flightService.buscarOfertasDeVuelosParaIndex();

        // 2. PLAN B: Si la API no devuelve nada, usamos el scraper.
        if (ofertasDestacadas.isEmpty()) {
            logger.warn("La API de Amadeus no devolvió ofertas. Usando el ScrapingService como fallback.");
            ofertasDestacadas = scrapingService.scrapeOfertasPrincipales();
        }

        model.addAttribute("ofertas", ofertasDestacadas);

        // 3. Lógica de reservas del usuario (esto no cambia)
        if (principal != null) {
            usuarioDAO.findByEmail(principal.getName()).ifPresent(usuario -> {
                List<Reserva> misReservas = reservaDAO.findByUsuario(usuario);
                model.addAttribute("reservas", misReservas);
            });
        } else {
            model.addAttribute("reservas", Collections.emptyList());
        }

        return "index";
    }

    /**
     * Endpoint de prueba para verificar el funcionamiento del web scraping.
     */
    @GetMapping("/test-scraping")
    @ResponseBody
    public List<Oferta> testWebScraping() {
        return scrapingService.scrapeOfertasPrincipales();
    }

    // --- El resto de tus métodos del controlador permanecen igual ---

    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @GetMapping("/registrar")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrar";
    }

    @PostMapping("/registrar")
    public String processRegistration(Usuario usuario, RedirectAttributes redirectAttributes) {
        if (usuarioDAO.findByEmail(usuario.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado.");
            return "redirect:/registrar";
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol("USUARIO");
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuarioDAO.save(usuario);
        redirectAttributes.addFlashAttribute("success", "¡Registro exitoso! Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }

    @GetMapping("/perfil")
    public String showProfilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("No se encontró al usuario autenticado."));
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @GetMapping("/vuelos/buscar")
    public String showFlightResults(
            @RequestParam("origen") String origen,
            @RequestParam("destino") String destino,
            Model model) {
        model.addAttribute("origenBusqueda", origen);
        model.addAttribute("destinoBusqueda", destino);
        return "resultados-vuelos";
    }

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
}