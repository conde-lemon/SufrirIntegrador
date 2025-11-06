package com.travel4u.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("postgres")
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
            System.out.println("✓ Conexión a base de datos exitosa");
            System.out.println("URL: " + connection.getMetaData().getURL());
            System.out.println("Usuario: " + connection.getMetaData().getUserName());
        }
    }

    @Test
    public void testTablesExist() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            
            // Verificar tabla usuarios
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            assertTrue(rs.next());
            System.out.println("✓ Tabla usuarios: " + rs.getInt(1) + " registros");

            // Verificar tabla reserva
            rs = stmt.executeQuery("SELECT COUNT(*) FROM reserva");
            assertTrue(rs.next());
            System.out.println("✓ Tabla reserva: " + rs.getInt(1) + " registros");

            // Verificar tabla servicio
            rs = stmt.executeQuery("SELECT COUNT(*) FROM servicio");
            assertTrue(rs.next());
            System.out.println("✓ Tabla servicio: " + rs.getInt(1) + " registros");
        }
    }

    @Test
    public void testReportQuery() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            
            // Probar la query del reporte
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
                WHERE r.id_usuario = 5
                ORDER BY COALESCE(r.created_at, CURRENT_TIMESTAMP) DESC
                """;
            
            ResultSet rs = stmt.executeQuery(query);
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("Reserva " + count + ": " + rs.getString("codigo_reserva") + 
                                 " - " + rs.getString("estado") + " - " + rs.getBigDecimal("total"));
            }
            System.out.println("✓ Query de reporte ejecutada: " + count + " resultados");
        }
    }
}