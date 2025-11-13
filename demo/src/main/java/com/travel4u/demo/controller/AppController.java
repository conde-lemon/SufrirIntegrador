// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/controller/AppController.java
package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import com.travel4u.demo.oferta.model.Oferta;
import com.travel4u.demo.oferta.repository.IOfertaDAO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AppController {

    // MEJORA: Inyección de dependencias por constructor (práctica recomendada)
    private final IReservaDAO reservaDAO;
    private final IUsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;
    private final IOfertaDAO ofertaDAO;

    public AppController(IReservaDAO reservaDAO, IUsuarioDAO usuarioDAO, PasswordEncoder passwordEncoder, IOfertaDAO ofertaDAO) {
        this.reservaDAO = reservaDAO;
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
        this.ofertaDAO = ofertaDAO;
    }

    /**
     * Muestra la página de inicio con reservas del usuario autenticado (si está logueado).
     */
    @GetMapping("/")
    public String viewHomePage(Model model, Principal principal) {
        System.out.println("[DEBUG] Iniciando carga de página de inicio...");
        
        try {
            List<Reserva> listaReservas;
            
            if (principal != null) {
                // Usuario autenticado: mostrar sus reservas
                String email = principal.getName();
                Usuario usuario = usuarioDAO.findByEmail(email).orElse(null);
                
                if (usuario != null) {
                    listaReservas = reservaDAO.findByUsuarioOrderByCreatedAtDesc(usuario);
                    System.out.println("[DEBUG] Reservas del usuario " + email + ": " + listaReservas.size());
                } else {
                    listaReservas = java.util.Collections.emptyList();
                }
            } else {
                // Usuario no autenticado: mostrar reservas de ejemplo (últimas 3)
                listaReservas = reservaDAO.findAll().stream().limit(3).toList();
                System.out.println("[DEBUG] Reservas de ejemplo: " + listaReservas.size());
            }
            
            for (int i = 0; i < listaReservas.size() && i < 3; i++) {
                Reserva r = listaReservas.get(i);
                System.out.println("[DEBUG] Reserva " + (i+1) + ": ID=" + r.getIdReserva() + 
                                 ", Estado=" + r.getEstado() + 
                                 ", Total=" + r.getTotal() + 
                                 ", Usuario=" + (r.getUsuario() != null ? r.getUsuario().getEmail() : "null"));
            }
            
            model.addAttribute("reservas", listaReservas);
            
            // Cargar ofertas de Amadeus para mostrar en el index
            List<Oferta> ofertas = ofertaDAO.findAll();
            model.addAttribute("ofertas", ofertas);
            System.out.println("[DEBUG] Ofertas cargadas: " + ofertas.size());
            
            System.out.println("[DEBUG] Modelo configurado correctamente");
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar reservas: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("reservas", java.util.Collections.emptyList());
            model.addAttribute("ofertas", java.util.Collections.emptyList());
        }
        
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
     */
    @PostMapping("/registrar")
    public String processRegistration(Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si el email ya existe
            if (usuarioDAO.findByEmail(usuario.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado.");
                return "redirect:/registrar";
            }

            // MEJORA DE SEGURIDAD: Encriptar la contraseña antes de guardarla
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // Establecer valores por defecto
            usuario.setRol("USUARIO");
            usuario.setActivo(true);
            usuario.setFechaRegistro(LocalDateTime.now());
            
            // Asegurar que el ID sea null para que Hibernate genere uno nuevo
            usuario.setIdUsuario(null);

            usuarioDAO.save(usuario);

            redirectAttributes.addFlashAttribute("success", "¡Registro exitoso! Ahora puedes iniciar sesión.");
            return "redirect:/login";
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al registrar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al registrar el usuario. Por favor, inténtalo de nuevo.");
            return "redirect:/registrar";
        }
    }

    /**
     * Muestra el perfil del usuario autenticado.
     */
    @GetMapping("/perfil")
    public String showProfilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        // Obtenemos el email del usuario logueado y lo buscamos en la BD
        String email = principal.getName();
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("No se encontró al usuario autenticado."));

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    // --- CORRECCIÓN ---
    // Se eliminó el método showFlightResults que causaba el conflicto con VueloController.
    // La lógica para "/vuelos/buscar" ahora está centralizada en VueloController.java


    // --- Métodos para páginas estáticas ---
    // NOTA: Los endpoints /vuelos, /cruceros, /bus ahora están en ServicioController

    @GetMapping("/hospedaje")
    public String showHospedajePage() {
        return "hospedaje";
    }

    @GetMapping("/paquetes-y-promociones")
    public String showOfertasPage() {
        // Lógica para mostrar ofertas, por ahora redirige a una página de ejemplo
        return "ofertas"; // Asume que tienes una plantilla ofertas.html
    }

    @GetMapping("/terminos-y-condiciones")
    public String showTerminosPage() {
        return "terminos_y_condiciones"; // Asume que tienes un terminos_y_condiciones.html
    }

    @GetMapping("/amadeus-extractor")
    public String showAmadeusExtractorPage() {
        return "amadeus-extractor";
    }




    @GetMapping("/confirmacion-reserva")
    public String showConfirmacionReservaPage(
            @RequestParam(required = false) Long idReserva,
            Model model,
            Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        // Obtener el usuario autenticado
        String email = principal.getName();
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        // Si se pasó un ID de reserva, buscarla
        String codigoReserva = "TFU-2025-0000";
        if (idReserva != null) {
            // Generar código de reserva basado en el ID
            codigoReserva = String.format("TFU-%d-%04d",
                    java.time.LocalDate.now().getYear(),
                    idReserva);
        }

        // Pasar datos al template
        model.addAttribute("idUsuario", usuario.getIdUsuario());
        model.addAttribute("codigoReserva", codigoReserva);

        return "confirmacion-reserva";
    }
}