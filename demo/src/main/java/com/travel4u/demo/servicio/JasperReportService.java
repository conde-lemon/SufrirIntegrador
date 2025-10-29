package com.travel4u.demo.servicio;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    /**
     * Genera un reporte usando conexión a base de datos
     * Los .jrxml tienen queries SQL
     *
     * @param reportFileName Nombre del archivo .jrxml (sin extensión)
     * @param parameters Parámetros del reporte (ej: ID_USUARIO)
     * @param format Formato de salida: "pdf", "xlsx", "html"
     * @return Bytes del reporte generado
     */
    public byte[] generateReport(String reportFileName, Map<String, Object> parameters, String format) throws Exception {

        Connection connection = null;

        try {
            System.out.println("=== JasperReportService: Iniciando generación de reporte ===");
            System.out.println("Archivo: " + reportFileName);
            System.out.println("Parámetros: " + parameters);
            System.out.println("Formato: " + format);

            // Obtener conexión de la base de datos
            connection = dataSource.getConnection();
            System.out.println("✓ Conexión a BD obtenida");

            JasperReport jasperReport = null;

            // Intentar cargar el archivo .jasper precompilado primero
            String jasperPath = "reports/" + reportFileName + ".jasper";
            ClassPathResource jasperResource = new ClassPathResource(jasperPath);

            if (jasperResource.exists()) {
                System.out.println("⚡ Usando archivo precompilado: " + jasperPath);
                InputStream jasperStream = jasperResource.getInputStream();
                jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
                System.out.println("✓ Reporte .jasper cargado");
            } else {
                // Si no existe .jasper, compilar desde .jrxml
                System.out.println(" Compilando desde .jrxml");

                // Prioridad 1: ReservasTest (más robusto, con manejo de sin datos)
                String jrxmlPath = "reports/ReservasTest.jrxml";
                ClassPathResource jrxmlResource = new ClassPathResource(jrxmlPath);

                if (!jrxmlResource.exists()) {
                    // Prioridad 2: Intentar con el backup simple
                    jrxmlPath = "reports/" + reportFileName + "_Backup.jrxml";
                    jrxmlResource = new ClassPathResource(jrxmlPath);
                }

                if (!jrxmlResource.exists()) {
                    // Prioridad 3: Si no existe el backup, usar el original
                    jrxmlPath = "reports/" + reportFileName + ".jrxml";
                    jrxmlResource = new ClassPathResource(jrxmlPath);
                }

                if (!jrxmlResource.exists()) {
                    throw new Exception("Archivo de reporte no encontrado: " + reportFileName);
                }

                System.out.println("✓ Archivo encontrado: " + jrxmlPath);
                InputStream reportStream = jrxmlResource.getInputStream();

                // Compilar el reporte
                jasperReport = JasperCompileManager.compileReport(reportStream);
                System.out.println("✓ Reporte compilado exitosamente desde .jrxml");
            }

            // Llenar el reporte con la conexión y parámetros
            System.out.println(" Parámetros del reporte:");
            System.out.println("   - ID_USUARIO: " + parameters.get("ID_USUARIO"));
            System.out.println("   - ESTADO_FILTRO: " + parameters.get("ESTADO_FILTRO"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            int numPaginas = jasperPrint.getPages().size();
            System.out.println("✓ Reporte llenado con datos. Páginas: " + numPaginas);

            if (numPaginas == 0) {
                System.out.println("️  ADVERTENCIA: El reporte no tiene páginas.");
                System.out.println("   Esto puede indicar que:");
                System.out.println("   1. No hay datos que coincidan con los parámetros");
                System.out.println("   2. La query SQL no retorna resultados");
                System.out.println("   3. Verifica con: SELECT * FROM reserva WHERE id_usuario = " + parameters.get("ID_USUARIO"));
            } else {
                System.out.println("✓ El reporte tiene contenido para mostrar");
            }

            // Exportar según el formato solicitado
            byte[] result = exportReport(jasperPrint, format);
            System.out.println("✓ Reporte exportado. Tamaño: " + result.length + " bytes");

            return result;

        } catch (Exception e) {
            System.err.println(" ERROR en JasperReportService:");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Tipo: " + e.getClass().getName());

            // Si es un error de XML/UUID, dar un mensaje más claro
            if (e.getMessage() != null && e.getMessage().contains("uuid")) {
                System.err.println(" SOLUCIÓN: El archivo .jrxml tiene atributos 'uuid' incompatibles con JasperReports 6.21.0");
                System.err.println("   Usando archivo simplificado '_Backup.jrxml'");
            }

            e.printStackTrace();
            throw e;
        } finally {
            // Cerrar la conexión
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión a BD cerrada");
            }
        }
    }

    /**
     * Exporta el reporte al formato especificado
     */
    private byte[] exportReport(JasperPrint jasperPrint, String format) throws JRException {
        switch (format.toLowerCase()) {
            case "pdf":
                return JasperExportManager.exportReportToPdf(jasperPrint);

            case "xlsx":
            case "excel":
                return exportToExcel(jasperPrint);

            case "html":
                return JasperExportManager.exportReportToHtmlFile(String.valueOf(jasperPrint)).getBytes();

            default:
                throw new IllegalArgumentException("Formato no soportado: " + format);
        }
    }

    /**
     * Exporta a Excel
     */
    private byte[] exportToExcel(JasperPrint jasperPrint) throws JRException {
        net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter exporter =
                new net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();

        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();

        exporter.setExporterInput(new net.sf.jasperreports.export.SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new net.sf.jasperreports.export.SimpleOutputStreamExporterOutput(outputStream));

        net.sf.jasperreports.export.SimpleXlsxReportConfiguration configuration =
                new net.sf.jasperreports.export.SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);

        exporter.setConfiguration(configuration);
        exporter.exportReport();

        return outputStream.toByteArray();
    }
}