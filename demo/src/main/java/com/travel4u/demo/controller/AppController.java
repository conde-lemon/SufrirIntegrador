package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private IReservaDAO reservaDAO;

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Muestra la página de inicio y le pasa la lista de todas las reservas.
     */
    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Reserva> listaReservas = reservaDAO.findAll();
        model.addAttribute("reservas", listaReservas);
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
        // Verificar si el email ya existe
        if (usuarioDAO.findByEmail(usuario.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado.");
            return "redirect:/registrar";
        }

        // Guardar contraseña en texto plano (sin encriptar)
        // NOTA: Solo para desarrollo, en producción usar BCrypt
        // Establecer valores por defecto
        usuario.setRol("USER");
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
        // Obtenemos el email del usuario logueado y lo buscamos en la BD
        String email = principal.getName();
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("No se encontró al usuario autenticado."));

        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    /**
     * Redirección por rol después del login
     */
    /*@GetMapping("/redirect-by-role")
    public String redirectByRole(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // DEBUG
        System.out.println("=== 🚨 REDIRECCIÓN POR ROL 🚨 ===");
        System.out.println("Usuario: " + authentication.getName());
        for (GrantedAuthority authority : authorities) {
            System.out.println("Rol: '" + authority.getAuthority() + "'");
        }
        System.out.println("================================");

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/vistadmin/dashboard";
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                return "redirect:/vuelos";
            }
        }

        return "redirect:/";
    }
*/
    /**
     * Muestra la página de resultados de búsqueda de vuelos.
     */
    @GetMapping("/vuelos/buscar")
    public String showFlightResults(
            @RequestParam("origen") String origen,
            @RequestParam("destino") String destino,
            Model model) {

        // Pasamos los parámetros de búsqueda al modelo para que la plantilla los pueda usar
        model.addAttribute("origenBusqueda", origen);
        model.addAttribute("destinoBusqueda", destino);

        // Aquí iría la lógica para buscar vuelos en la base de datos.
        // Por ahora, solo mostramos una página de resultados de ejemplo.
        // List<Vuelo> vuelosEncontrados = vueloService.buscarVuelos(origen, destino);
        // model.addAttribute("vuelos", vuelosEncontrados);

        return "resultados-vuelos"; // Renderiza la plantilla 'resultados-vuelos.html'
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
        // Lógica para mostrar ofertas, por ahora redirige a una página de ejemplo
        return "ofertas"; // Asume que tienes una plantilla ofertas.html
    }

    @GetMapping("/reservas")
    public String showReservasPage() {
        // Lógica para mostrar las reservas del usuario
        return "reservas"; // Asume que tienes una plantilla reservas.html
    }

    @GetMapping("/adminasdad/vistadmin")
    public String showAdminDashboard(Authentication authentication, Model model) {
        System.out.println("=== ACCEDIENDO AL PANEL ADMIN ===");
        System.out.println("Admin: " + authentication.getName());

        // Opcional: Agregar datos al modelo si necesitas
        model.addAttribute("usuario", authentication.getName());

        return "vistadmin"; // Esta vista debe existir
    }

    @GetMapping("/terminos-y-condiciones")
    public String showTerminosPage() {
        return "terminos_y_condiciones"; // Asume que tienes un terminos_y_condiciones.html
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