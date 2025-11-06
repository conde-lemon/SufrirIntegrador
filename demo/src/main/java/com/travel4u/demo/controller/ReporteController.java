package com.travel4u.demo.controller;

import com.travel4u.demo.servicio.JasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private JasperReportService jasperReportService;

    @GetMapping("/reservas")
    public ResponseEntity<byte[]> generarReporteReservas(
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "pdf") String formato) {
        
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ID_USUARIO", idUsuario);
            parametros.put("ESTADO_FILTRO", "%");
            
            byte[] reporte = jasperReportService.generateReport("Reservas", parametros, formato);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reservas_Usuario_" + idUsuario + ".pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<byte[]> generarReporteUsuarios(
            @RequestParam(defaultValue = "pdf") String formato) {
        
        try {
            Map<String, Object> parametros = new HashMap<>();
            
            byte[] reporte = jasperReportService.generateReport("Reporte-usuario", parametros, formato);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Usuarios.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/promociones")
    public ResponseEntity<byte[]> generarReportePromociones(
            @RequestParam(defaultValue = "pdf") String formato) {
        
        try {
            Map<String, Object> parametros = new HashMap<>();
            
            byte[] reporte = jasperReportService.generateReport("Reporte-Promociones", parametros, formato);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Reporte_Promociones.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}