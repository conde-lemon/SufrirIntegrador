package com.travel4u.demo.servicio;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    public byte[] generateReport(String reportName, Map<String, Object> parameters, String format) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            
            // Cargar el archivo .jrxml
            ClassPathResource resource = new ClassPathResource("reports/" + reportName + ".jrxml");
            InputStream reportStream = resource.getInputStream();
            
            // Compilar el reporte
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            // Llenar el reporte con datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            
            // Exportar a PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            if ("pdf".equalsIgnoreCase(format)) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                exporter.exportReport();
            } else {
                throw new IllegalArgumentException("Formato no soportado: " + format);
            }
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            System.err.println("Error generando reporte " + reportName + ": " + e.getMessage());
            throw e;
        }
    }
}