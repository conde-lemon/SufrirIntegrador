// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/controller/AppController.java
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
import org.springframework.web.bind.annotation.ResponseBody; // ¡Importante!
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private IReservaDAO reservaDAO;

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScrapingService scrapingService;

    /**
     * Muestra la página de inicio con ofertas destacadas y la lista de reservas recientes.
     */
    @GetMapping("/")
    public String viewHomePage(Model model, Principal principal) { // Añadimos Principal
        // 1. Obtenemos las ofertas del web scraper (esto no cambia).
        List<Oferta> ofertasDestacadas = scrapingService.scrapeOfertasPrincipales();
        model.addAttribute("ofertas", ofertasDestacadas);

        // 2. CORRECCIÓN DE SEGURIDAD: Obtenemos solo las reservas del usuario logueado.
        if (principal != null) {
            // Si hay un usuario logueado, buscamos sus reservas.
            usuarioDAO.findByEmail(principal.getName()).ifPresent(usuario -> {
                List<Reserva> misReservas = reservaDAO.findByUsuario(usuario);
                model.addAttribute("reservas", misReservas);
            });
        } else {
            // Si no hay nadie logueado, pasamos una lista vacía para evitar errores.
            model.addAttribute("reservas", Collections.emptyList());
        }

        return "index";
    }


    /**
     * NUEVO: Endpoint de prueba para verificar el funcionamiento del web scraping.
     * Devuelve los resultados en formato JSON para una fácil revisión.
     *
     * Para usarlo, simplemente accede a esta URL en tu navegador: http://localhost:8080/test-scraping
     *
     * @return Una lista de las ofertas extraídas en formato JSON.
     */
    @GetMapping("/test-scraping")
    @ResponseBody // Esta anotación es clave: devuelve los datos directamente, sin buscar una plantilla HTML.
    public List<Oferta> testWebScraping() {
        // Llama al servicio y devuelve el resultado.
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