package com.travel4u.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/diagnostic")
public class DiagnosticController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            response.put("status", "SUCCESS");
            response.put("url", connection.getMetaData().getURL());
            response.put("user", connection.getMetaData().getUserName());
            response.put("driver", connection.getMetaData().getDriverName());
            
            Map<String, Integer> tableCounts = new HashMap<>();
            String[] tables = {"usuarios", "reserva", "servicio", "oferta"};
            
            for (String table : tables) {
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
                    if (rs.next()) {
                        tableCounts.put(table, rs.getInt(1));
                    }
                } catch (Exception e) {
                    tableCounts.put(table, -1);
                }
            }
            
            response.put("tableCounts", tableCounts);
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservas-data")
    public ResponseEntity<Map<String, Object>> testReservasData(@RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            
            String query = """
                SELECT r.id_reserva, r.estado, r.total, u.email 
                FROM reserva r 
                INNER JOIN usuarios u ON r.id_usuario = u.id_usuario 
                WHERE r.id_usuario = """ + userId;
            
            ResultSet rs = stmt.executeQuery(query);
            
            java.util.List<Map<String, Object>> reservas = new java.util.ArrayList<>();
            while (rs.next()) {
                Map<String, Object> reserva = new HashMap<>();
                reserva.put("id", rs.getLong("id_reserva"));
                reserva.put("estado", rs.getString("estado"));
                reserva.put("total", rs.getBigDecimal("total"));
                reserva.put("email", rs.getString("email"));
                reservas.add(reserva);
            }
            
            response.put("status", "SUCCESS");
            response.put("total", reservas.size());
            response.put("reservas", reservas);
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}