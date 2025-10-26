package com.travel4u.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // Usar NoOpPasswordEncoder para aceptar contraseñas en texto plano
        // NOTA: Solo para desarrollo, NO usar en producción
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/registrar", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/vistadmin/**", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/perfil", "/reservas", "/vuelos/**").hasAnyRole("USER", "ADMIN")
                     
                        // Rutas de reportes - requieren autenticación
                        .requestMatchers("/api/reportes/**").authenticated()
                        // Las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirect-by-role", true) // ← CAMBIAR AQUÍ
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // IMPORTANTE: Deshabilitar CSRF para endpoints de API (reportes)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/reportes/**")
                );

        return http.build();
    }
}