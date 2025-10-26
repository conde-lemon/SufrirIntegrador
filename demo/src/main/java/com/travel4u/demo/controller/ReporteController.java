package com.travel4u.demo.controller;

import com.travel4u.demo.servicio.JasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private JasperReportService jasperReportService;

    /**
     * Endpoint para generar reporte de reservas
     * Usado por: confirmacion-reserva.html
     * URL: /api/reportes/reservas?idUsuario=1&estado=confirmada
     *
     * El reporte usa el .jrxml: Reservas.jrxml
     * Parámetros del JRXML: ID_USUARIO, ESTADO_FILTRO
     */
    @GetMapping("/reservas")
    public ResponseEntity<byte[]> generarReporteReservas(
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "pdf") String formato) {

        try {
            // Si no se proporciona idUsuario, retornar error
            if (idUsuario == null) {
                System.err.println("ERROR: idUsuario es null");
                return new ResponseEntity<>(
                    "Error: Se requiere el ID de usuario".getBytes(),
                    HttpStatus.BAD_REQUEST
                );
            }

            System.out.println("=== GENERANDO REPORTE DE RESERVAS ===");
            System.out.println("ID Usuario: " + idUsuario);
            System.out.println("Estado filtro: " + (estado != null ? estado : "TODOS"));
            System.out.println("Formato: " + formato);

            // Preparar parámetros para el reporte según el JRXML
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ID_USUARIO", idUsuario);

            // El JRXML usa LIKE para filtrar, necesita formato SQL
            // Si no hay estado o es vacío, mostrar todos
            parametros.put("ESTADO_FILTRO", "%"); // Todos los estados por defecto

            // Generar el reporte (usa la query SQL del JRXML)
            byte[] reporte = jasperReportService.generateReport(
                    "Reservas",  // Archivo: Reservas.jrxml
                    parametros,
                    formato
            );

            System.out.println("✓ Reporte generado exitosamente. Tamaño: " + reporte.length + " bytes");

            // Configurar headers de respuesta
            HttpHeaders headers = new HttpHeaders();

            String nombreArchivo = "Mis_Reservas_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            switch (formato.toLowerCase()) {
                case "pdf":
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", nombreArchivo + ".pdf");
                    break;

                case "xlsx":
                case "excel":
                    headers.setContentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                    headers.setContentDispositionFormData("attachment", nombreArchivo + ".xlsx");
                    break;

                case "html":
                    headers.setContentType(MediaType.TEXT_HTML);
                    break;

                default:
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", nombreArchivo + ".pdf");
            }

            return new ResponseEntity<>(reporte, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println(" ERROR al generar reporte:");
            e.printStackTrace();

            String errorMsg = "Error al generar el reporte: " + e.getMessage();
            return new ResponseEntity<>(
                errorMsg.getBytes(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Endpoint para reporte de promociones
     * URL: /api/reportes/promociones
     *
     * El reporte usa el .jrxml: Reporte-Promociones.jrxml
     * Este JRXML no necesita parámetros (trae todas las ofertas)
     */
    @GetMapping("/promociones")
    public ResponseEntity<byte[]> generarReportePromociones(
            @RequestParam(defaultValue = "pdf") String formato) {

        try {
            // Este reporte no necesita parámetros específicos
            Map<String, Object> parametros = new HashMap<>();

            byte[] reporte = jasperReportService.generateReport(
                    "Reporte-Promociones",
                    parametros,
                    formato
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String nombreArchivo = "Promociones_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            headers.setContentDispositionFormData("attachment", nombreArchivo);

            return new ResponseEntity<>(reporte, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para reporte de usuarios
     * URL: /api/reportes/usuarios
     *
     * El reporte usa el .jrxml: Reporte-usuario.jrxml
     * Este JRXML no necesita parámetros (trae todos los usuarios)
     */
    @GetMapping("/usuarios")
    public ResponseEntity<byte[]> generarReporteUsuarios(
            @RequestParam(defaultValue = "pdf") String formato) {

        try {
            Map<String, Object> parametros = new HashMap<>();

            byte[] reporte = jasperReportService.generateReport(
                    "Reporte-usuario",
                    parametros,
                    formato
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String nombreArchivo = "Usuarios_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            headers.setContentDispositionFormData("attachment", nombreArchivo);

            return new ResponseEntity<>(reporte, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}