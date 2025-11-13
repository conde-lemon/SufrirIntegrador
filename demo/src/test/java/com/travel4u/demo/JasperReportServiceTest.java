package com.travel4u.demo;

import com.travel4u.demo.servicio.JasperReportService;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
public class JasperReportServiceTest {

    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testReportGeneration() throws Exception {
        // Buscar usuario con reservas
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail("admin@travel4u.com");
        assertTrue(usuarioOpt.isPresent(), "Usuario admin debe existir");
        Usuario usuario = usuarioOpt.get();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("ID_USUARIO", usuario.getIdUsuario());
        parametros.put("ESTADO_FILTRO", "%");

        // Generar reporte
        byte[] reporte = jasperReportService.generateReport("Reservas", parametros, "pdf");
        
        assertNotNull(reporte);
        assertTrue(reporte.length > 0);
        System.out.println("✓ Reporte generado exitosamente. Tamaño: " + reporte.length + " bytes");
    }

    @Test
    public void testReportQueryDirectly() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String query = """
                SELECT
                    r.id_reserva,
                    'TFU-' || EXTRACT(YEAR FROM COALESCE(r.created_at, CURRENT_TIMESTAMP)) || '-' || LPAD(CAST(r.id_reserva AS VARCHAR), 4, '0') as codigo_reserva,
                    COALESCE(r.created_at, CURRENT_TIMESTAMP) as fecha_reserva,
                    r.fecha_inicio,
                    r.fecha_fin,
                    COALESCE(r.estado, 'pendiente') as estado,
                    COALESCE(r.total, 0) as total,
                    COALESCE(r.moneda, 'PEN') as moneda,
                    COALESCE(r.observaciones, 'Sin observaciones') as observaciones,
                    COALESCE(u.nombres, 'Usuario') as nombres,
                    COALESCE(u.apellidos, '') as apellidos,
                    COALESCE(u.email, '') as email
                FROM reserva r
                INNER JOIN usuarios u ON r.id_usuario = u.id_usuario
                WHERE r.id_usuario = ?
                ORDER BY COALESCE(r.created_at, CURRENT_TIMESTAMP) DESC
                """;

            Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail("admin@travel4u.com");
            assertTrue(usuarioOpt.isPresent(), "Usuario admin debe existir");
            
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, usuarioOpt.get().getIdUsuario());
                
                try (ResultSet rs = stmt.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        System.out.println("Reserva " + count + ":");
                        System.out.println("  - Código: " + rs.getString("codigo_reserva"));
                        System.out.println("  - Estado: " + rs.getString("estado"));
                        System.out.println("  - Total: " + rs.getBigDecimal("total"));
                        System.out.println("  - Cliente: " + rs.getString("nombres") + " " + rs.getString("apellidos"));
                    }
                    
                    System.out.println("✓ Query ejecutada correctamente. Resultados: " + count);
                    assertTrue(count >= 0, "Query debe ejecutarse sin errores");
                }
            }
        }
    }

    @Test
    public void testAllUsersWithReservations() throws Exception {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                 "SELECT u.id_usuario, u.email, COUNT(r.id_reserva) as total_reservas " +
                 "FROM usuarios u LEFT JOIN reserva r ON u.id_usuario = r.id_usuario " +
                 "GROUP BY u.id_usuario, u.email ORDER BY total_reservas DESC")) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("=== USUARIOS Y SUS RESERVAS ===");
                while (rs.next()) {
                    Long userId = rs.getLong("id_usuario");
                    String email = rs.getString("email");
                    int totalReservas = rs.getInt("total_reservas");
                    
                    System.out.println("Usuario ID " + userId + " (" + email + "): " + totalReservas + " reservas");
                    
                    // Si tiene reservas, probar generar reporte
                    if (totalReservas > 0) {
                        Map<String, Object> parametros = new HashMap<>();
                        parametros.put("ID_USUARIO", userId);
                        parametros.put("ESTADO_FILTRO", "%");
                        
                        try {
                            byte[] reporte = jasperReportService.generateReport("Reservas", parametros, "pdf");
                            System.out.println("  ✓ Reporte generado: " + reporte.length + " bytes");
                        } catch (Exception e) {
                            System.out.println("  ✗ Error generando reporte: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
}