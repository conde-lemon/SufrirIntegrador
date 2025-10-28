package com.travel4u.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/registrar", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/vistadmin/**", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/","/perfil", "/reservas", "/vuelos", "/verperfil").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())  // ← USA EL SUCCESS HANDLER
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                org.springframework.security.core.Authentication authentication)
                    throws IOException, ServletException {

                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

                System.out.println("===  DEBUG ROLES DESPUÉS DEL LOGIN ===");
                System.out.println("Usuario: " + authentication.getName());
                for (GrantedAuthority authority : authorities) {
                    System.out.println("Rol detectado: '" + authority.getAuthority() + "'");
                }

                String targetUrl = "/"; // Por defecto para usuarios normales

                for (GrantedAuthority authority : authorities) {
                    if (authority.getAuthority().equals("ROLE_ADMIN")) {
                        targetUrl = "/vistadmin";  // ← Redirige a /vistadmin
                        System.out.println(" Redirigiendo ADMIN a: " + targetUrl);
                        break;
                    } else if (authority.getAuthority().equals("ROLE_USER")) {
                        targetUrl = "/";
                        System.out.println(" Redirigiendo USER a: " + targetUrl);
                        break;
                    }
                }

                System.out.println("🎯 URL final: " + targetUrl);
                response.sendRedirect(targetUrl);
            }
        };
    }
}