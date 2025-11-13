package com.travel4u.demo.servicio;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(JasperReportService.class);

    @Autowired
    private DataSource dataSource;

    public byte[] generateReport(String reportName, Map<String, Object> parameters, String format) throws Exception {
        logger.info("=== INICIANDO GENERACIÓN DE REPORTE ===" );
        logger.info("Nombre del reporte: {}", reportName);
        logger.info("Formato solicitado: {}", format);
        logger.info("Parámetros recibidos: {}", parameters.size());
        
        Connection connection = null;
        try {
            // Obtener conexión a la base de datos
            logger.info("Obteniendo conexión a la base de datos...");
            connection = dataSource.getConnection();
            logger.info("Conexión obtenida exitosamente: {}", connection.getMetaData().getURL());
            
            // Cargar el archivo .jrxml
            String reportPath = "reports/" + reportName + ".jrxml";
            logger.info("Cargando archivo de reporte: {}", reportPath);
            ClassPathResource resource = new ClassPathResource(reportPath);
            
            if (!resource.exists()) {
                logger.error("ARCHIVO DE REPORTE NO ENCONTRADO: {}", reportPath);
                throw new RuntimeException("Archivo de reporte no encontrado: " + reportPath);
            }
            
            InputStream reportStream = resource.getInputStream();
            logger.info("Archivo de reporte cargado exitosamente");
            
            // Compilar el reporte
            logger.info("Compilando reporte...");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            logger.info("Reporte compilado exitosamente");
            
            // Llenar el reporte con datos
            logger.info("Llenando reporte con datos...");
            logger.info("Parámetros para el reporte:");
            parameters.forEach((key, value) -> logger.info("  {} = {} ({})", key, value, value != null ? value.getClass().getSimpleName() : "null"));
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            logger.info("Reporte llenado exitosamente. Páginas generadas: {}", jasperPrint.getPages().size());
            
            if (jasperPrint.getPages().isEmpty()) {
                logger.warn("ADVERTENCIA: El reporte no generó ninguna página. Posibles causas:");
                logger.warn("  - No hay datos que coincidan con los parámetros");
                logger.warn("  - Error en la consulta SQL del reporte");
                logger.warn("  - Parámetros incorrectos");
            }
            
            // Exportar a PDF
            logger.info("Exportando a formato: {}", format);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            if ("pdf".equalsIgnoreCase(format)) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                exporter.exportReport();
                logger.info("Exportación a PDF completada");
            } else {
                logger.error("Formato no soportado: {}", format);
                throw new IllegalArgumentException("Formato no soportado: " + format);
            }
            
            byte[] result = outputStream.toByteArray();
            logger.info("=== REPORTE GENERADO EXITOSAMENTE ===" );
            logger.info("Tamaño del archivo generado: {} bytes", result.length);
            return result;
            
        } catch (Exception e) {
            logger.error("=== ERROR EN GENERACIÓN DE REPORTE ===" );
            logger.error("Reporte: {}", reportName);
            logger.error("Tipo de error: {}", e.getClass().getSimpleName());
            logger.error("Mensaje: {}", e.getMessage());
            logger.error("Stack trace completo:", e);
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    logger.info("Conexión a base de datos cerrada");
                } catch (Exception e) {
                    logger.warn("Error al cerrar conexión: {}", e.getMessage());
                }
            }
        }
    }
}