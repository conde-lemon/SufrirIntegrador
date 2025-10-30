package com.travel4u.demo.controller;

import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Controlador para las funciones administrativas.
 * Solo accesible por usuarios con rol ADMIN.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IUsuarioDAO usuarioDAO;

    public AdminController(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Muestra el panel de administración.
     * Solo accesible por usuarios con rol ADMIN.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String showAdminPanel(Model model, Principal principal) {
        // Verificar que el usuario está autenticado
        if (principal == null) {
            return "redirect:/login";
        }

        // Obtener información del usuario admin
        String email = principal.getName();
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        // Verificar que el usuario tiene rol ADMIN
        if (!"ADMIN".equals(usuario.getRol())) {
            // Si no es admin, redirigir a la página de inicio
            return "redirect:/";
        }

        // Agregar datos del usuario al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("adminName", usuario.getNombres() + " " + usuario.getApellidos());

        return "ADMIN/admin";
    }
}

