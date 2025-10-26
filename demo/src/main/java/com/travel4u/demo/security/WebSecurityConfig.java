package com.travel4u.demo.security;

// Ya no necesitamos importar UserDetailsServiceImpl ni DaoAuthenticationProvider
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // El codificador de contraseñas sigue siendo necesario para el registro de usuarios.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ¡HEMOS QUITADO LOS BEANS de userDetailsService y authenticationProvider!

    // ... (resto de la clase)

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 1. CORRECCIÓN: Quitamos "/" de la lista pública.
                        // Ahora solo las rutas explícitas son públicas.
                        .requestMatchers(
                                // LA RUTA "/" YA NO ESTÁ AQUÍ
                                "/login",
                                "/registrar",
                                "/test-scraping",
                                "/api/amadeus/**",
                                "/css/**",
                                "/js/**",
                                "/img/**"
                        ).permitAll()
                        // 2. CUALQUIER OTRA ruta (incluida "/") requiere que el usuario esté autenticado.
                        .anyRequest().authenticated()
                )
                // ... (el resto de la configuración de formLogin y logout no cambia)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
