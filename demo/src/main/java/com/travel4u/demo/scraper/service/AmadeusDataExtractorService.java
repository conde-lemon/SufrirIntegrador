package com.travel4u.demo.scraper.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel4u.demo.oferta.model.Oferta;
import com.travel4u.demo.oferta.repository.IOfertaDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class AmadeusDataExtractorService {

    private static final Logger logger = LoggerFactory.getLogger(AmadeusDataExtractorService.class);
    private final Amadeus amadeus;
    private final ObjectMapper objectMapper;
    private final IOfertaDAO ofertasDAO;

    public AmadeusDataExtractorService(Amadeus amadeus, IOfertaDAO ofertasDAO) {
        this.amadeus = amadeus;
        this.objectMapper = new ObjectMapper();
        this.ofertasDAO = ofertasDAO;
    }

    public void extraerDatosParaPromociones() {
        logger.info("=== INICIANDO EXTRACCIÓN DE DATOS AMADEUS ===");

        List<RutaVuelo> rutasPopulares = Arrays.asList(
                new RutaVuelo("LIM", "MAD", "Lima a Madrid"),
                new RutaVuelo("LIM", "MIA", "Lima a Miami"),
                new RutaVuelo("LIM", "CUZ", "Lima a Cusco"),
                new RutaVuelo("BOG", "LIM", "Bogotá a Lima"),
                new RutaVuelo("SCL", "LIM", "Santiago a Lima")
        );

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        for (RutaVuelo ruta : rutasPopulares) {
            try {
                extraerDatosRuta(ruta, timestamp);
                // Pausa para no saturar la API de Amadeus
                Thread.sleep(1000);
            } catch (Exception e) {
                logger.error("Error extrayendo datos para ruta {}: {}", ruta.descripcion, e.getMessage());
            }
        }

        logger.info("=== EXTRACCIÓN COMPLETADA ===");
    }

    private void extraerDatosRuta(RutaVuelo ruta, String timestamp) throws ResponseException, IOException {
        logger.info("Extrayendo datos para: {}", ruta.descripcion);

        try {
            String fechaSalida = LocalDateTime.now().plusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            FlightOfferSearch[] ofertas = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", ruta.origen)
                            .and("destinationLocationCode", ruta.destino)
                            .and("departureDate", fechaSalida)
                            .and("adults", 1)
                            .and("max", 10)
            );

            File dir = new File("amadeus_data");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = String.format("amadeus_data/ofertas_%s_%s_to_%s_%s.json",
                    ruta.origen, ruta.destino, timestamp, ofertas.length);

            try (FileWriter writer = new FileWriter(filename)) {
                String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ofertas);
                writer.write(jsonData);

                logger.info("✓ Guardado: {} ofertas en {}", ofertas.length, filename);

                if (ofertas.length > 0) {
                    FlightOfferSearch primera = ofertas[0];
                    logger.info("  Precio ejemplo: {} {}",
                            primera.getPrice().getTotal(),
                            primera.getPrice().getCurrency());
                    logger.info("  Aerolínea: {}",
                            primera.getValidatingAirlineCodes()[0]);
                }
            }

            generarResumenPromocional(ruta, ofertas, timestamp);
            guardarOfertasEnBD(ruta, ofertas);

        } catch (ResponseException e) {
            logger.warn("API Amadeus falló para {}, generando datos simulados", ruta.descripcion);
            generarDatosSimulados(ruta, timestamp);
        }
    }

    private void generarDatosSimulados(RutaVuelo ruta, String timestamp) {
        try {
            // Generar precios simulados
            double precioBase = 300 + (Math.random() * 800); // Entre 300 y 1100
            String[] aerolineas = {"LA", "AV", "IB", "AA", "UA"};
            String aerolinea = aerolineas[(int)(Math.random() * aerolineas.length)];

            // Crear archivo JSON simulado
            File dir = new File("amadeus_data");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = String.format("amadeus_data/ofertas_simuladas_%s_to_%s_%s.json",
                    ruta.origen, ruta.destino, timestamp);

            // CORRECCIÓN: Reemplazar el text block por una cadena concatenada para máxima compatibilidad.
            String jsonSimulado = String.format(
                    "{\n" +
                            "  \"ruta\": \"%s\",\n" +
                            "  \"precio\": %.2f,\n" +
                            "  \"moneda\": \"USD\",\n" +
                            "  \"aerolinea\": \"%s\",\n" +
                            "  \"origen\": \"%s\",\n" +
                            "  \"destino\": \"%s\",\n" +
                            "  \"fecha_consulta\": \"%s\",\n" +
                            "  \"tipo\": \"simulado\"\n" +
                            "}",
                    ruta.descripcion,
                    precioBase,
                    aerolinea,
                    ruta.origen,
                    ruta.destino,
                    LocalDateTime.now()
            );

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(jsonSimulado);
                logger.info("✓ Datos simulados guardados: {}", filename);
            }

            // Guardar en BD
            Oferta nuevaOferta = new Oferta();
            nuevaOferta.setNombre("Vuelo " + ruta.descripcion);
            nuevaOferta.setDescripcion("Aerolínea: " + aerolinea + ". Oferta especial simulada.");
            nuevaOferta.setPrecio(BigDecimal.valueOf(precioBase));
            nuevaOferta.setDescuento(BigDecimal.valueOf(15.0));
            nuevaOferta.setFechaInicio(LocalDateTime.now());
            nuevaOferta.setFechaFin(LocalDateTime.now().plusDays(30));
            nuevaOferta.setEtiquetas("simulado,vuelo,promocion," + ruta.origen + "," + ruta.destino);
            nuevaOferta.setUrl("/vuelos");
            nuevaOferta.setFuente("SIMULADO");
            nuevaOferta.setFechaExtraccion(LocalDateTime.now());
            nuevaOferta.setActiva(true);

            ofertasDAO.save(nuevaOferta);
            logger.info("✓ Oferta simulada guardada en BD: {} - ${}", ruta.descripcion, String.format("%.2f", precioBase));

        } catch (Exception e) {
            logger.error("Error generando datos simulados: {}", e.getMessage());
        }
    }

    private void generarResumenPromocional(RutaVuelo ruta, FlightOfferSearch[] ofertas, String timestamp) {
        try {
            String filename = String.format("amadeus_data/resumen_promocional_%s_to_%s_%s.txt",
                    ruta.origen, ruta.destino, timestamp);

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("=== RESUMEN PROMOCIONAL ===\n");
                writer.write("Ruta: " + ruta.descripcion + "\n");
                writer.write("Fecha consulta: " + LocalDateTime.now() + "\n");
                writer.write("Total ofertas: " + ofertas.length + "\n\n");

                if (ofertas.length > 0) {
                    double precioMinimo = Arrays.stream(ofertas)
                            .mapToDouble(o -> Double.parseDouble(o.getPrice().getTotal()))
                            .min().orElse(0.0);

                    writer.write("PRECIO MÁS BAJO: " + precioMinimo + " " + ofertas[0].getPrice().getCurrency() + "\n");

                    writer.write("\nAEROLÍNEAS DISPONIBLES:\n");
                    Arrays.stream(ofertas)
                            .map(o -> o.getValidatingAirlineCodes()[0])
                            .distinct()
                            .forEach(airline -> {
                                try {
                                    writer.write("- " + airline + "\n");
                                } catch (IOException e) {
                                    logger.error("Error escribiendo aerolínea", e);
                                }
                            });

                    writer.write("\n=== DATOS PARA PROMOCIÓN ===\n");
                    writer.write("Título sugerido: \"¡Vuelos desde $" + (int)precioMinimo + " a " + ruta.descripcion.split(" a ")[1] + "!\"\n");
                    writer.write("Descripción: \"Encuentra los mejores precios para volar de " + ruta.descripcion + "\"\n");
                    writer.write("Tags: \"oferta,vuelo,internacional,promocion\"\n");
                }
            }

            logger.info("✓ Resumen promocional guardado: {}", filename);

        } catch (IOException e) {
            logger.error("Error generando resumen promocional", e);
        }
    }

    public String extraerDatosRutaEspecifica(String origen, String destino, String fecha) {
        try {
            FlightOfferSearch[] ofertas = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", origen)
                            .and("destinationLocationCode", destino)
                            .and("departureDate", fecha)
                            .and("adults", 1)
                            .and("max", 5)
            );

            String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ofertas);
            logger.info("JSON extraído para {} a {}: {} ofertas", origen, destino, ofertas.length);

            return jsonData;

        } catch (Exception e) {
            logger.error("Error extrayendo datos específicos: {}", e.getMessage());
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }

    private void guardarOfertasEnBD(RutaVuelo ruta, FlightOfferSearch[] ofertas) {
        try {
            for (FlightOfferSearch oferta : ofertas) {
                double precio = Double.parseDouble(oferta.getPrice().getTotal());

                Oferta nuevaOferta = new Oferta();
                nuevaOferta.setNombre("Vuelo " + ruta.descripcion);
                nuevaOferta.setDescripcion("Aerolínea: " + oferta.getValidatingAirlineCodes()[0] +
                        ". Duración: " + oferta.getItineraries()[0].getDuration());
                nuevaOferta.setPrecio(BigDecimal.valueOf(precio));
                nuevaOferta.setDescuento(BigDecimal.valueOf(15.0));
                nuevaOferta.setFechaInicio(LocalDateTime.now());
                nuevaOferta.setFechaFin(LocalDateTime.now().plusDays(30));
                nuevaOferta.setEtiquetas("amadeus,vuelo,promocion," + ruta.origen + "," + ruta.destino);
                nuevaOferta.setUrl("/vuelos");
                nuevaOferta.setFuente("AMADEUS_API");
                nuevaOferta.setFechaExtraccion(LocalDateTime.now());
                nuevaOferta.setActiva(true);

                ofertasDAO.save(nuevaOferta);
            }

            logger.info("✓ Guardadas {} ofertas en BD para ruta {}", ofertas.length, ruta.descripcion);

        } catch (Exception e) {
            logger.error("Error guardando ofertas en BD: {}", e.getMessage());
        }
    }

    private static class RutaVuelo {
        final String origen;
        final String destino;
        final String descripcion;

        RutaVuelo(String origen, String destino, String descripcion) {
            this.origen = origen;
            this.destino = destino;
            this.descripcion = descripcion;
        }
    }
}