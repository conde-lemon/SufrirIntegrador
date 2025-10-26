package com.travel4u.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/redirect-by-role")
    public String redirectByRole(Authentication authentication) {
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return "redirect:/vistadmin";  // Admin → administración
            }
        }
        return "redirect:/";  // Usuario normal → página principal
    }
}
