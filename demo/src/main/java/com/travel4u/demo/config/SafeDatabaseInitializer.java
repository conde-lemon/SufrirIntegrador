package com.travel4u.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * DESHABILITADO: Ya no se necesita inicialización automática de datos
 * La base de datos de Supabase ya contiene los datos necesarios
 *
 * Para habilitar nuevamente, descomenta @Component
 */
// @Component
public class SafeDatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Inicialización deshabilitada
        System.out.println("SafeDatabaseInitializer deshabilitado - No se ejecutarán scripts SQL automáticos");
    }
}