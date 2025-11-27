package com.travel4u.demo.servicio;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.util.Collection;
import java.util.Map;

@Service
public class JasperReportService {

    private static final Logger logger = LoggerFactory.getLogger(JasperReportService.class);

    @Autowired(required = false) // No es requerido para el nuevo método
    private DataSource dataSource;

    /**
     * Genera un reporte usando una colección de beans como fuente de datos.
     *
     * @param reportName Nombre del archivo de reporte (sin .jrxml)
     * @param parameters Mapa de parámetros para el reporte
     * @param dataSourceCollection Colección de objetos (beans) para el datasource
     * @param format Formato de salida (ej. "pdf")
     * @return byte[] con el contenido del reporte
     * @throws Exception Si ocurre un error durante la generación
     */
    public byte[] generateReportFromCollection(String reportName, Map<String, Object> parameters,
                                               Collection<?> dataSourceCollection, String format) throws Exception {
        logger.info("=== INICIANDO GENERACIÓN DE REPORTE DESDE COLECCIÓN ===");
        logger.info("Reporte: {}, Formato: {}", reportName, format);

        try {
            // 1. Cargar y compilar el reporte
            String reportPath = "reports/" + reportName + ".jrxml";
            ClassPathResource resource = new ClassPathResource(reportPath);
            if (!resource.exists()) {
                logger.error("ARCHIVO NO ENCONTRADO: {}", reportPath);
                throw new RuntimeException("No se encontró el archivo de reporte: " + reportPath);
            }
            InputStream reportStream = resource.getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            logger.info("Reporte compilado exitosamente.");

            // 2. Crear el JRDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataSourceCollection);
            logger.info("JRBeanCollectionDataSource creado con {} elementos.", dataSourceCollection.size());

            // 3. Llenar el reporte
            logger.info("Llenando el reporte con parámetros y datasource...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            logger.info("Reporte llenado. Páginas: {}", jasperPrint.getPages().size());

            // 4. Exportar el reporte
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if ("pdf".equalsIgnoreCase(format)) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                exporter.exportReport();
                logger.info("Exportación a PDF finalizada.");
            } else {
                throw new IllegalArgumentException("Formato no soportado: " + format);
            }

            logger.info("=== REPORTE (DESDE COLECCIÓN) TERMINADO ===");
            return outputStream.toByteArray();

        } catch (Exception e) {
            logger.error("ERROR generando reporte {} desde colección", reportName, e);
            throw e;
        }
    }

    /**
     * @deprecated Usar generateReportFromCollection para reportes basados en beans.
     * Este método se mantiene por retrocompatibilidad si otros reportes lo usan.
     */
    @Deprecated
    public byte[] generateReport(String reportName, Map<String, Object> parameters, String format) throws Exception {
        logger.info("=== INICIANDO GENERACIÓN DE REPORTE (LEGACY) ===");
        if (dataSource == null) {
            throw new IllegalStateException("DataSource no está configurado. Este método requiere una conexión a BD.");
        }
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Conexión obtenida exitosamente a: {}", connection.getMetaData().getURL());

            String reportPath = "reports/" + reportName + ".jrxml";
            ClassPathResource resource = new ClassPathResource(reportPath);
            if (!resource.exists()) {
                logger.error("ARCHIVO NO ENCONTRADO: {}", reportPath);
                throw new RuntimeException("No se encontró el archivo de reporte: " + reportPath);
            }
            InputStream reportStream = resource.getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            logger.info("Llenando reporte (legacy)...");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if ("pdf".equalsIgnoreCase(format)) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                exporter.exportReport();
            } else {
                throw new IllegalArgumentException("Formato no soportado: " + format);
            }

            logger.info("=== REPORTE (LEGACY) TERMINADO ===");
            return outputStream.toByteArray();

        } catch (Exception e) {
            logger.error("ERROR generando reporte {} (legacy)", reportName, e);
            throw e;
        }
    }
}
