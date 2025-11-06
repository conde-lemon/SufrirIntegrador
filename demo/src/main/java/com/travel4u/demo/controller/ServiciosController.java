package com.travel4u.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
public class ServiciosController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/add-missing")
    public ResponseEntity<Map<String, Object>> addMissingServices() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Añadir proveedores faltantes
            String[] proveedores = {
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'American Airlines', 'AEROLINEA', 'contacto@aa.com', 'contacto@aa.com', '400500600', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'American Airlines')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Air France', 'AEROLINEA', 'contacto@airfrance.com', 'contacto@airfrance.com', '400500601', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Air France')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Lufthansa', 'AEROLINEA', 'contacto@lufthansa.com', 'contacto@lufthansa.com', '400500602', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Lufthansa')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Emirates', 'AEROLINEA', 'contacto@emirates.com', 'contacto@emirates.com', '400500603', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Emirates')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Qatar Airways', 'AEROLINEA', 'contacto@qatarairways.com', 'contacto@qatarairways.com', '400500604', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Qatar Airways')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Royal Caribbean', 'CRUCERO', 'contacto@royalcaribbean.com', 'contacto@royalcaribbean.com', '200300400', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Royal Caribbean')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Norwegian Cruise Line', 'CRUCERO', 'contacto@ncl.com', 'contacto@ncl.com', '200300401', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Norwegian Cruise Line')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Celebrity Cruises', 'CRUCERO', 'contacto@celebrity.com', 'contacto@celebrity.com', '200300402', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Celebrity Cruises')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'MSC Cruceros', 'CRUCERO', 'contacto@msc.com', 'contacto@msc.com', '200300403', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'MSC Cruceros')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Princess Cruises', 'CRUCERO', 'contacto@princess.com', 'contacto@princess.com', '200300404', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Princess Cruises')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Cruz del Sur', 'BUS', 'contacto@cruzdelsur.com', 'contacto@cruzdelsur.com', '300400500', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Cruz del Sur')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Oltursa', 'BUS', 'contacto@oltursa.com', 'contacto@oltursa.com', '300400501', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Oltursa')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Movil Tours', 'BUS', 'contacto@moviltours.com', 'contacto@moviltours.com', '300400502', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Movil Tours')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Civa', 'BUS', 'contacto@civa.com', 'contacto@civa.com', '300400503', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Civa')",
                
                "INSERT INTO proveedor (nombre, tipo_proveedor, contacto, email, telefono, activo) " +
                "SELECT 'Tepsa', 'BUS', 'contacto@tepsa.com', 'contacto@tepsa.com', '300400504', true " +
                "WHERE NOT EXISTS (SELECT 1 FROM proveedor WHERE nombre = 'Tepsa')"
            };

            // Ejecutar inserts de proveedores
            int proveedoresAdded = 0;
            for (String sql : proveedores) {
                proveedoresAdded += jdbcTemplate.update(sql);
            }

            // Añadir servicios
            String[] servicios = {
                // Vuelos
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'VUELO', 'Lima a Roma', 'Peru', 'Italia', 'internacional,historia,cultura', 1450.00, 8, 'Descubre la Ciudad Eterna y su historia milenaria', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'American Airlines' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Roma')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'VUELO', 'Lima a Amsterdam', 'Peru', 'Holanda', 'internacional,europa,canales', 1320.00, 12, 'Explora los canales y museos de Amsterdam', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Air France' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Amsterdam')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'VUELO', 'Lima a Berlin', 'Peru', 'Alemania', 'internacional,historia,cultura', 1380.00, 10, 'Capital alemana llena de historia y modernidad', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Lufthansa' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Berlin')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'VUELO', 'Lima a Doha', 'Peru', 'Qatar', 'internacional,lujo,conexion', 1550.00, 6, 'Hub internacional de lujo en el Golfo Pérsico', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Qatar Airways' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Doha')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'VUELO', 'Lima a Los Angeles', 'Peru', 'Estados Unidos', 'internacional,entretenimiento,playa', 920.00, 15, 'Ciudad de los Ángeles y Hollywood', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Emirates' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Los Angeles')",
                
                // Cruceros
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'CRUCERO', 'Crucero Caribe Occidental', 'Miami', 'Caribe', 'crucero,playa,tropical,familiar', 899.00, 20, 'Crucero de 7 días por las islas del Caribe Occidental', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Royal Caribbean' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Caribe Occidental')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'CRUCERO', 'Crucero Mediterráneo Clásico', 'Barcelona', 'Mediterráneo', 'crucero,cultura,historia,europa', 1299.00, 15, 'Crucero de 10 días por las costas del Mediterráneo', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Norwegian Cruise Line' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Mediterráneo Clásico')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'CRUCERO', 'Crucero Fiordos Noruegos', 'Bergen', 'Noruega', 'crucero,naturaleza,paisajes,aventura', 1899.00, 12, 'Crucero de 12 días por los espectaculares fiordos noruegos', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Celebrity Cruises' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Fiordos Noruegos')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'CRUCERO', 'Crucero Transatlántico', 'Southampton', 'New York', 'crucero,lujo,relajacion,oceano', 2199.00, 8, 'Crucero transatlántico de 14 días con máximo lujo', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'MSC Cruceros' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Transatlántico')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'CRUCERO', 'Crucero Alaska Glaciares', 'Seattle', 'Alaska', 'crucero,naturaleza,glaciares,vida salvaje', 1699.00, 10, 'Crucero de 8 días para ver glaciares y vida salvaje', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Princess Cruises' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Crucero Alaska Glaciares')",
                
                // Buses
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'BUS', 'Lima a Huacachina', 'Peru', 'Peru', 'bus,nacional,oasis,aventura', 45.00, 30, 'Viaje cómodo al oasis de Huacachina en Ica', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Cruz del Sur' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Huacachina')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'BUS', 'Lima a Paracas', 'Peru', 'Peru', 'bus,nacional,playa,naturaleza', 38.00, 35, 'Excursión a las Islas Ballestas y Reserva de Paracas', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Oltursa' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Paracas')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'BUS', 'Lima a Nazca', 'Peru', 'Peru', 'bus,nacional,misterio,lineas', 55.00, 25, 'Descubre las misteriosas Líneas de Nazca', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Movil Tours' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Nazca')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'BUS', 'Lima a Ayacucho', 'Peru', 'Peru', 'bus,nacional,historia,cultura', 85.00, 20, 'Ciudad histórica con arquitectura colonial', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Civa' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Ayacucho')",
                
                "INSERT INTO servicio (tipo_servicio, nombre, origen, destino, tags, precio_base, disponibilidad, descripcion, id_proveedor, activo) " +
                "SELECT 'BUS', 'Lima a Huaraz', 'Peru', 'Peru', 'bus,nacional,montaña,trekking', 75.00, 22, 'Puerta de entrada a la Cordillera Blanca', " +
                "(SELECT id_proveedor FROM proveedor WHERE nombre = 'Tepsa' LIMIT 1), true " +
                "WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Lima a Huaraz')"
            };

            // Ejecutar inserts de servicios
            int serviciosAdded = 0;
            for (String sql : servicios) {
                serviciosAdded += jdbcTemplate.update(sql);
            }

            // Obtener conteo final
            Integer totalServicios = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM servicio", Integer.class);
            
            response.put("success", true);
            response.put("message", "Servicios añadidos exitosamente");
            response.put("proveedoresAdded", proveedoresAdded);
            response.put("serviciosAdded", serviciosAdded);
            response.put("totalServicios", totalServicios);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al añadir servicios: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getServicesCount() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Integer totalServicios = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM servicio", Integer.class);
            
            response.put("success", true);
            response.put("totalServicios", totalServicios);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener conteo: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}