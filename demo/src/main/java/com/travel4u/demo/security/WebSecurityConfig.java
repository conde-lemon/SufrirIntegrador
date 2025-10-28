package com.travel4u.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// ¡CAMBIO! Importamos el PasswordEncoder que no hace nada (No Operation)
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * ADVERTENCIA: Se usa NoOpPasswordEncoder, que NO encripta las contraseñas.
     * Las contraseñas se guardan y comparan en texto plano.
     * Esto es INSEGURO y solo debe usarse para fines de depuración o demostración.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Devuelve una instancia que trata las contraseñas como texto plano.
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * Define qué rutas son públicas, cuáles requieren autenticación y cómo se manejan
     * el login, logout y CSRF.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas accesibles sin autenticación
                        .requestMatchers("/login", "/registrar", "/css/**", "/js/**", "/img/**", "/").permitAll()
                        // Rutas de API de reportes requieren autenticación
                        .requestMatchers("/api/reportes/**").authenticated()
                        // Todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Especifica la página de login personalizada
                        .defaultSuccessUrl("/", true) // Redirige a la raíz después de un login exitoso
                        .permitAll() // Permite el acceso a la página de login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirige a la página de login con mensaje de logout
                        .permitAll() // Permite el acceso a la URL de logout
                )
                // IMPORTANTE: Deshabilitar CSRF para endpoints de API (como los de reportes)
                // si se van a consumir desde clientes externos que no manejan tokens CSRF.
                // Para formularios HTML gestionados por Thymeleaf, CSRF suele ser útil.
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/reportes/**") // Ignora CSRF para la API de reportes
                );

        return http.build();
    }
}