package com.travel4u.demo.security;

// Ya no necesitamos importar UserDetailsServiceImpl ni DaoAuthenticationProvider
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

    // El codificador de contraseñas sigue siendo necesario para el registro de usuarios.
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // Usar NoOpPasswordEncoder para aceptar contraseñas en texto plano
        // NOTA: Solo para desarrollo, NO usar en producción
        return NoOpPasswordEncoder.getInstance();
    }

    // ¡HEMOS QUITADO LOS BEANS de userDetailsService y authenticationProvider!

    // ... (resto de la clase)

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers("/login", "/registrar", "/css/**", "/js/**", "/img/**").permitAll()
                        // Rutas de reportes - requieren autenticación
                        .requestMatchers("/api/reportes/**").authenticated()
                        // Las demás rutas requieren autenticación
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
                )
                // IMPORTANTE: Deshabilitar CSRF para endpoints de API (reportes)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/reportes/**")
                );

        return http.build();
    }
}
