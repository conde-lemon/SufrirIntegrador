package com.travel4u.demo.security;

import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador personalizado para redirigir a los usuarios después del login
 * según su rol.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final IUsuarioDAO usuarioDAO;

    public CustomAuthenticationSuccessHandler(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();
        Usuario usuario = usuarioDAO.findByEmail(email).orElse(null);

        if (usuario != null && "ADMIN".equals(usuario.getRol())) {
            // Si es admin, redirigir al panel de administración
            response.sendRedirect("/admin");
        } else {
            // Si es usuario normal, redirigir a la página de inicio
            response.sendRedirect("/");
        }
    }
}

