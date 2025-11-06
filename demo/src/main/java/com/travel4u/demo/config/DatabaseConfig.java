package com.travel4u.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    @Profile("postgres")
    public CommandLineRunner databaseHealthCheck() {
        return args -> {
            System.out.println("=== VERIFICACIÓN DE BASE DE DATOS ===");
            
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("✓ Conexión exitosa a: " + connection.getMetaData().getURL());
                System.out.println("✓ Usuario: " + connection.getMetaData().getUserName());
                System.out.println("✓ Driver: " + connection.getMetaData().getDriverName());
                
                // Verificar tablas principales
                checkTable(connection, "usuarios");
                checkTable(connection, "reserva");
                checkTable(connection, "servicio");
                checkTable(connection, "oferta");
                
                // Verificar datos de prueba
                checkTestData(connection);
                
            } catch (Exception e) {
                System.err.println("✗ Error de conexión a BD: " + e.getMessage());
                System.err.println("  Verificar que PostgreSQL esté ejecutándose en puerto 8180");
                System.err.println("  Verificar que la BD 'sufrirIntegrador' exista");
                System.err.println("  Verificar credenciales: postgres/conde-lemon");
            }
        };
    }
    
    private void checkTable(Connection connection, String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("✓ Tabla " + tableName + ": " + count + " registros");
            }
        } catch (Exception e) {
            System.err.println("✗ Error verificando tabla " + tableName + ": " + e.getMessage());
        }
    }
    
    private void checkTestData(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            // Verificar usuario admin
            ResultSet rs = stmt.executeQuery("SELECT email FROM usuarios WHERE email = 'admin@travel4u.com'");
            if (rs.next()) {
                System.out.println("✓ Usuario admin encontrado");
            } else {
                System.out.println("⚠ Usuario admin no encontrado");
            }
            
            // Verificar reservas con datos completos
            rs = stmt.executeQuery("""
                SELECT COUNT(*) as total FROM reserva r 
                INNER JOIN usuarios u ON r.id_usuario = u.id_usuario 
                WHERE r.total IS NOT NULL AND r.estado IS NOT NULL
                """);
            
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("✓ Reservas válidas para reportes: " + total);
                
                if (total == 0) {
                    System.out.println("⚠ No hay reservas válidas. Los reportes estarán vacíos.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error verificando datos de prueba: " + e.getMessage());
        }
    }
}