package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.servicio.JasperReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoletaController {

    private static final Logger logger = LoggerFactory.getLogger(BoletaController.class);

    @Autowired
    private IReservaDAO reservaRepository;

    @Autowired
    private JasperReportService jasperReportService;

    @GetMapping("/boletas")
    public String listarReservas(Model model) {
        logger.info("=== ACCEDIENDO A PÁGINA DE BOLETAS ===");
        
        try {
            List<Reserva> reservas = reservaRepository.findAll();
            logger.info("Reservas encontradas: {}", reservas.size());
            
            for (Reserva reserva : reservas) {
                logger.debug("Reserva ID: {}, Usuario: {}, Total: {}", 
                    reserva.getIdReserva(), 
                    reserva.getUsuario() != null ? reserva.getUsuario().getEmail() : "Sin usuario",
                    reserva.getTotal());
            }
            
            model.addAttribute("reservas", reservas);
            return "boletas-lista";
            
        } catch (Exception e) {
            logger.error("ERROR al cargar reservas: ", e);
            model.addAttribute("error", "Error al cargar las reservas: " + e.getMessage());
            return "boletas-lista";
        }
    }

    @GetMapping("/boleta/reserva/{id}/preview")
    public String previsualizarBoleta(@PathVariable Integer id, Model model) {
        logger.info("=== PREVISUALIZANDO BOLETA PARA RESERVA ID: {} ===", id);
        
        try {
            Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
            
            model.addAttribute("reserva", reserva);
            return "boleta-preview";
            
        } catch (Exception e) {
            logger.error("Error al cargar vista previa de boleta: ", e);
            model.addAttribute("error", "Error al cargar la boleta: " + e.getMessage());
            return "redirect:/boletas";
        }
    }

    @GetMapping("/boleta/reserva/{id}/pdf")
    public ResponseEntity<byte[]> generarBoletaPDF(@PathVariable Integer id) {
        logger.info("=== GENERANDO BOLETA PARA RESERVA ID: {} ===", id);
        
        try {
            // Buscar la reserva
            logger.info("Buscando reserva con ID: {}", id);
            Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
            
            logger.info("Reserva encontrada: {}", reserva.getIdReserva());
            logger.info("Usuario de la reserva: {}", reserva.getUsuario() != null ? reserva.getUsuario().getEmail() : "NULL");
            logger.info("Total de la reserva: {}", reserva.getTotal());
            logger.info("Estado de la reserva: {}", reserva.getEstado());

            // Preparar parámetros para el reporte
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("codigoReserva", "TFU-" + reserva.getIdReserva());
            parametros.put("fechaEmision", new Date());
            
            if (reserva.getUsuario() != null) {
                String nombreCompleto = (reserva.getUsuario().getNombres() != null ? reserva.getUsuario().getNombres() : "") + 
                                       " " + (reserva.getUsuario().getApellidos() != null ? reserva.getUsuario().getApellidos() : "");
                parametros.put("clienteNombre", nombreCompleto.trim());
                parametros.put("clienteEmail", reserva.getUsuario().getEmail() != null ? reserva.getUsuario().getEmail() : "");
            } else {
                logger.warn("Usuario es NULL para reserva ID: {}", id);
                parametros.put("clienteNombre", "Cliente no disponible");
                parametros.put("clienteEmail", "");
            }
            
            parametros.put("total", reserva.getTotal() != null ? reserva.getTotal() : BigDecimal.ZERO);
            parametros.put("moneda", reserva.getMoneda() != null ? reserva.getMoneda() : "PEN");
            parametros.put("estado", reserva.getEstado() != null ? reserva.getEstado() : "Pendiente");
            parametros.put("observaciones", reserva.getObservaciones() != null ? reserva.getObservaciones() : "Sin observaciones");

            logger.info("Parámetros preparados para el reporte:");
            parametros.forEach((key, value) -> logger.info("  {} = {}", key, value));

            // Generar el reporte
            logger.info("Iniciando generación del reporte PDF...");
            byte[] reporte = jasperReportService.generateReport("boleta-reserva", parametros, "pdf");
            logger.info("Reporte generado exitosamente. Tamaño: {} bytes", reporte.length);

            // Configurar headers de respuesta
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "Boleta_TFU-" + reserva.getIdReserva() + ".pdf");

            logger.info("=== BOLETA GENERADA EXITOSAMENTE ===");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);

        } catch (Exception e) {
            logger.error("=== ERROR AL GENERAR BOLETA ===");
            logger.error("Reserva ID: {}", id);
            logger.error("Tipo de error: {}", e.getClass().getSimpleName());
            logger.error("Mensaje de error: {}", e.getMessage());
            logger.error("Stack trace completo: ", e);
            
            return ResponseEntity.internalServerError()
                    .body(("Error al generar la boleta: " + e.getMessage()).getBytes());
        }
    }
}