//package com.travel4u.demo.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Deshabilitar CSRF solo si es necesario para APIs REST
//                .csrf(csrf -> csrf.disable())  // Deshabilita CSRF. Usa solo si estás construyendo una API REST.
//
//                // Configuración de autorización
//                .authorizeHttpRequests(authz -> authz
//                        // Permite acceso sin autenticación a las siguientes rutas
//                        .requestMatchers("/", "/login", "/registrar", "/vuelos", "/cruceros", "/bus", "/hospedaje", "/paquetes-y-promociones")
//                        .permitAll()
//
//                        // Solo ADMIN puede acceder a esta URL
//                        .requestMatchers("/vistadmin/**")
//                        .hasRole("ADMIN")
//
//                        // Cualquier otra solicitud requiere autenticación
//                        .anyRequest().authenticated()
//                )
//
//                // Configuración del inicio de sesión y la página de inicio de sesión personalizada
//                .formLogin(form -> form
//                        .loginPage("/login")  // Página de login personalizada
//                        .permitAll()  // Permite que cualquiera vea la página de login
//                        .defaultSuccessUrl("/vistadmin", true)  // Redirigir a /vistadmin después de un login exitoso
//                )
//
//                // Configuración del cierre de sesión
//                .logout(logout -> logout
//                        .permitAll()  // Permite que cualquiera cierre sesión
//                        .logoutSuccessUrl("/login")  // Redirige a login después de cerrar sesión
//                );
//
//        return http.build();
//    }
//}

