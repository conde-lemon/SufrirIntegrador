package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.model.Detalle_Reserva;
import com.travel4u.demo.reserva.repository.IDetalleReservaDAO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping
public class BoletaController {

    private static final Logger logger = LoggerFactory.getLogger(BoletaController.class);

    @Autowired
    private IReservaDAO reservaRepository;

    @Autowired
    private JasperReportService jasperReportService;

    // Endpoint REST para devolver boletas como JSON para el panel admin
    @GetMapping(path = "/api/boletas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> listarBoletasJson() {
        try {
            List<Reserva> reservas = reservaRepository.findAll();
            List<Map<String, Object>> result = new ArrayList<>();

            for (Reserva r : reservas) {
                Map<String, Object> item = new HashMap<>();
                item.put("idBoleta", r.getIdReserva());
                String cliente = r.getUsuario() != null ? (r.getUsuario().getNombres() + " " + r.getUsuario().getApellidos()) : "N/A";
                item.put("usuario", cliente);

                // Servicio: tomar el primer detalle si existe
                String servicioNombre = "N/A";
                String fecha = r.getCreatedAt() != null ? r.getCreatedAt().toString() : null;
                String estado = r.getEstado();
                java.math.BigDecimal precio = r.getTotal();

                if (r.getDetalleReservas() != null && !r.getDetalleReservas().isEmpty()) {
                    Detalle_Reserva det = r.getDetalleReservas().iterator().next();
                    if (det.getServicio() != null) servicioNombre = det.getServicio().getNombre();
                    if (det.getSubtotal() != null) precio = det.getSubtotal();
                }

                item.put("servicio", servicioNombre);
                item.put("fecha", fecha);
                item.put("precio", precio != null ? precio.toString() : "0.00");
                item.put("estado", estado != null ? estado : "N/A");

                result.add(item);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error listando boletas JSON", e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }

    @GetMapping("/boletas")
    public String listarReservas(Model model) {
        List<Reserva> reservas = reservaRepository.findAll();
        model.addAttribute("reservas", reservas);
        return "boletas-lista";
    }

    @GetMapping("/boleta/reserva/{id}/preview")
    public String previsualizarBoleta(@PathVariable Integer id, Model model) {

        Reserva reserva = reservaRepository.findByIdWithDetails(id);
        if (reserva == null) {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }

        model.addAttribute("reserva", reserva);
        return "boleta-preview";
    }

    @GetMapping("/boleta/reserva/{id}/pdf")
    public ResponseEntity<byte[]> generarBoletaPDF(@PathVariable Integer id) {
        logger.info("=== GENERANDO BOLETA PDF PARA RESERVA ID: {} ===", id);

        try {
            // 1. Buscar la reserva con detalles y servicio con FETCH JOIN
            Reserva reserva = reservaRepository.findByIdWithDetails(id);
            if (reserva == null) {
                throw new RuntimeException("Reserva no encontrada con ID: " + id);
            }

            // Convertir Set -> List para Jasper
            List<Detalle_Reserva> detalles = new ArrayList<>(reserva.getDetalleReservas());

            if (detalles.isEmpty()) {
                throw new RuntimeException("La reserva existe pero NO contiene detalles.");
            }

            // 2. Preparar parámetros para Jasper
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_RESERVA_ID", reserva.getIdReserva().toString());
            parameters.put("P_FECHA_EMISION", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
            parameters.put("P_CLIENTE_NOMBRE", reserva.getUsuario().getNombres() + " " + reserva.getUsuario().getApellidos());
            parameters.put("P_CLIENTE_EMAIL", reserva.getUsuario().getEmail());
            parameters.put("P_MONEDA", reserva.getMoneda());
            parameters.put("P_TOTAL", reserva.getTotal());
            parameters.put("P_LOGO_STREAM",
                    this.getClass().getResourceAsStream("/static/img/logo.png"));


            // 3. Generar PDF
            byte[] reporte = jasperReportService.generateReportFromCollection(
                    "boleta-reserva",
                    parameters,
                    detalles,
                    "pdf"
            );

            // 4. Respuesta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "Boleta_TFU-" + reserva.getIdReserva() + ".pdf");

            logger.info("=== BOLETA PDF GENERADA EXITOSAMENTE PARA RESERVA ID: {} ===", id);
            return ResponseEntity.ok().headers(headers).body(reporte);

        } catch (Exception e) {
            logger.error("Error al generar la boleta PDF para la reserva ID: " + id, e);
            return ResponseEntity.internalServerError()
                    .body(("Error al generar la boleta: " + e.getMessage()).getBytes());
        }
    }

}
