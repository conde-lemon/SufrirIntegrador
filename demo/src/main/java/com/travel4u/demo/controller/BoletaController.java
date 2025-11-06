package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.servicio.JasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BoletaController {

    @Autowired
    private IReservaDAO reservaDAO;

    @Autowired
    private JasperReportService jasperReportService;

    @GetMapping("/boleta/reserva/{id}")
    public ResponseEntity<byte[]> generarBoleta(@PathVariable Integer id) {
        try {
            Reserva reserva = reservaDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("codigoReserva", "TFU-" + reserva.getIdReserva());
            parametros.put("fechaEmision", new Date());
            parametros.put("clienteNombre", reserva.getUsuario().getNombres() + " " + reserva.getUsuario().getApellidos());
            parametros.put("clienteEmail", reserva.getUsuario().getEmail());
            parametros.put("total", reserva.getTotal() != null ? reserva.getTotal() : BigDecimal.ZERO);
            parametros.put("moneda", reserva.getMoneda() != null ? reserva.getMoneda() : "PEN");
            parametros.put("estado", reserva.getEstado() != null ? reserva.getEstado() : "Pendiente");
            parametros.put("observaciones", reserva.getObservaciones() != null ? reserva.getObservaciones() : "Sin observaciones");

            byte[] reporte = jasperReportService.generateReport("boleta-reserva", parametros, "pdf");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Boleta_" + reserva.getIdReserva() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}