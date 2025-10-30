package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/boleta")
public class BoletaController {

    private final IReservaDAO reservaDAO;

    public BoletaController(IReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }

    @GetMapping("/reserva/{id}")
    public ResponseEntity<byte[]> generarBoleta(@PathVariable Integer id) {
        try {
            // Obtener la reserva
            Reserva reserva = reservaDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            // Cargar el template de JasperReports
            InputStream reportStream = new ClassPathResource("reports/boleta-reserva.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Preparar los datos
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("codigoReserva", "TFU-" + reserva.getIdReserva());
            parameters.put("fechaEmision", new Date());
            parameters.put("clienteNombre", reserva.getUsuario().getNombres() + " " + reserva.getUsuario().getApellidos());
            parameters.put("clienteEmail", reserva.getUsuario().getEmail());
            parameters.put("total", reserva.getTotal());
            parameters.put("moneda", reserva.getMoneda());
            parameters.put("estado", reserva.getEstado());
            parameters.put("observaciones", reserva.getObservaciones());

            // Crear datasource vacío (solo usamos parámetros)
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.emptyList());

            // Generar el PDF
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // Configurar headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Boleta_TFU-" + id + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}