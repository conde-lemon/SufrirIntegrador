package com.travel4u.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SafeDatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("=== INICIALIZACIÓN SEGURA DE BASE DE DATOS ===");
            
            // Verificar y crear tablas faltantes sin eliminar datos
            createMissingTables();
            
            // Insertar datos de ejemplo solo si no existen
            insertSampleDataIfEmpty();
            
            // Sincronizar secuencias
            syncSequences();
            
            System.out.println("✓ Inicialización completada - Datos existentes preservados");
            
            // Verificar estado final de la BD
            Integer totalUsuarios = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usuarios", Integer.class);
            Integer totalReservas = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reserva", Integer.class);
            Integer totalServicios = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM servicio", Integer.class);
            
            System.out.println("=== ESTADO FINAL DE LA BD ===");
            System.out.println("Usuarios: " + totalUsuarios);
            System.out.println("Reservas: " + totalReservas);
            System.out.println("Servicios: " + totalServicios);
            System.out.println("==============================");
            
        } catch (Exception e) {
            System.err.println("⚠ Error en inicialización: " + e.getMessage());
        }
    }
    
    private void createMissingTables() {
        // Crear tabla equipaje si no existe
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS public.equipaje (
                id_equipaje SERIAL PRIMARY KEY,
                tipo VARCHAR(100) NOT NULL,
                dimension_alto NUMERIC(8,2) NOT NULL,
                dimension_ancho NUMERIC(8,2) NOT NULL,
                dimension_largo NUMERIC(8,2) NOT NULL,
                peso_max NUMERIC(8,2) NOT NULL,
                precio NUMERIC(10,2) NOT NULL,
                descripcion TEXT,
                activo BOOLEAN DEFAULT true
            )
            """);
        
        // Crear tabla paquete si no existe
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS public.paquete (
                id_paquete SERIAL PRIMARY KEY,
                nombre VARCHAR(200) NOT NULL,
                descripcion TEXT,
                precio_total NUMERIC(10,2) NOT NULL,
                fecha_paquete TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                activo BOOLEAN DEFAULT true
            )
            """);
        
        System.out.println("✓ Tablas verificadas/creadas");
    }
    
    private void insertSampleDataIfEmpty() {
        // Insertar equipajes solo si la tabla está vacía
        Integer equipajeCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM equipaje", Integer.class);
        if (equipajeCount == 0) {
            jdbcTemplate.execute("""
                INSERT INTO public.equipaje (tipo, dimension_alto, dimension_ancho, dimension_largo, peso_max, precio, descripcion) VALUES
                ('Equipaje de Mano', 55.0, 40.0, 20.0, 8.0, 0.0, 'Equipaje de mano estándar incluido'),
                ('Equipaje Facturado 23kg', 75.0, 50.0, 30.0, 23.0, 50.0, 'Equipaje facturado estándar hasta 23kg'),
                ('Equipaje Extra 32kg', 75.0, 50.0, 30.0, 32.0, 80.0, 'Equipaje facturado extra hasta 32kg')
                """);
            System.out.println("✓ Datos de equipaje insertados");
        }
        
        // Insertar paquetes solo si la tabla está vacía
        Integer paqueteCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM paquete", Integer.class);
        if (paqueteCount == 0) {
            jdbcTemplate.execute("""
                INSERT INTO public.paquete (nombre, descripcion, precio_total) VALUES
                ('Paquete Cusco Mágico', 'Incluye vuelo, hotel 3 estrellas y tour a Machu Picchu', 850.00),
                ('Escapada a Europa', 'Vuelo a Madrid + 5 noches de hotel + city tour', 1800.00),
                ('Aventura Amazónica', 'Vuelo a Iquitos + lodge en la selva + excursiones', 650.00)
                """);
            System.out.println("✓ Datos de paquetes insertados");
        }
        
        // Verificar y reportar reservas existentes
        Integer reservaCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reserva", Integer.class);
        System.out.println("✓ Total reservas en BD: " + reservaCount);
        
        if (reservaCount > 0) {
            // Mostrar distribución por usuario
            jdbcTemplate.query(
                "SELECT u.email, COUNT(r.id_reserva) as total FROM usuarios u LEFT JOIN reserva r ON u.id_usuario = r.id_usuario GROUP BY u.id_usuario, u.email ORDER BY total DESC",
                (rs) -> {
                    System.out.println("  - " + rs.getString("email") + ": " + rs.getInt("total") + " reservas");
                }
            );
        }
    }
    
    private void syncSequences() {
        try {
            jdbcTemplate.execute("""
                SELECT setval(pg_get_serial_sequence('usuarios', 'id_usuario'), 
                (SELECT COALESCE(MAX(id_usuario), 0) + 1 FROM usuarios), false)
                """);
            System.out.println("✓ Secuencias sincronizadas");
        } catch (Exception e) {
            System.err.println("⚠ No se pudo sincronizar secuencias: " + e.getMessage());
        }
    }
}