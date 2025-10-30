package com.travel4u.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * ADVERTENCIA: Se usa NoOpPasswordEncoder, que NO encripta las contraseñas.
     * Esto es INSEGURO y solo debe usarse para fines de depuración.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder trata las contraseñas como texto plano.
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // En WebSecurityConfig.java, dentro del método securityFilterChain


        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers("/login", "/registrar", "/css/**", "/js/**", "/img/**").permitAll()
                        // NUEVO: Permitir acceso a la API de reservas si está autenticado
                        .requestMatchers("/api/reservas/**").authenticated()
                        // Todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Deshabilitar CSRF para simplificar, pero considera activarlo para producción
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}