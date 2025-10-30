package com.travel4u.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Sincronizar la secuencia de usuarios con los datos existentes
            String sql = "SELECT setval(pg_get_serial_sequence('usuarios', 'id_usuario'), " +
                        "(SELECT COALESCE(MAX(id_usuario), 0) + 1 FROM usuarios), false)";
            jdbcTemplate.execute(sql);
            System.out.println("[INFO] Secuencia de usuarios sincronizada correctamente");
        } catch (Exception e) {
            System.err.println("[WARNING] No se pudo sincronizar la secuencia de usuarios: " + e.getMessage());
        }
    }
}