package com.travel4u.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public WebSecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    /**
     * ADVERTENCIA: Se usa NoOpPasswordEncoder, que NO encripta las contraseñas.
     * Esto es INSEGURO y solo debe usarse para fines de depuración.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas que NO requieren autenticación
                        .requestMatchers("/login", "/registrar", "/css/**", "/js/**", "/img/**").permitAll()
                        // Rutas de administración - SOLO para usuarios con rol ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Rutas de API que sí requieren autenticación
                        .requestMatchers("/api/reportes/**").authenticated()
                        .requestMatchers("/api/ofertas/**").hasRole("ADMIN")
                        .requestMatchers("/api/paquetes/**").hasRole("ADMIN")
                        // Todas las demás rutas (incluida "/") requieren autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/reportes/**", "/api/ofertas/**", "/api/paquetes/**")
                );

        return http.build();
    }
}
