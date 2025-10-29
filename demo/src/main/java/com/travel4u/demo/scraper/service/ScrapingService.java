package com.travel4u.demo.scraper.service;

import com.travel4u.demo.scraper.model.Oferta;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScrapingService {

    // Es una buena práctica usar un logger para ver qué está pasando o si hay errores.
    private static final Logger logger = LoggerFactory.getLogger(ScrapingService.class);

    // URL de la sección de Skyscanner que vamos a scrapear.
    private static final String SCRAPING_URL = "https://www.espanol.skyscanner.com/";

    /**
     * Realiza un proceso de web scraping en Skyscanner para obtener ofertas de destinos populares.
     * Extrae el título, descripción, precio e imagen de las tarjetas de oferta.
     *
     * @return Una lista de objetos Oferta con los datos extraídos.
     */
    public List<Oferta> scrapeOfertasPrincipales() {
        List<Oferta> ofertas = new ArrayList<>();
        try {
            // 1. Conexión a la página web.
            // Usamos un User-Agent para simular que somos un navegador y evitar bloqueos.
            Document doc = Jsoup.connect(SCRAPING_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

            // 2. Selección de los elementos HTML que contienen las ofertas.
            // Este selector CSS apunta a las tarjetas de la sección "Explora cualquier lugar".
            // NOTA: Este selector puede cambiar si Skyscanner actualiza su web.
            Elements cards = doc.select("a.BpkLink_bpk-link__MWZlZ.DestinationsCards_card__YmQ4M");

            logger.info("Se encontraron {} tarjetas de oferta en Skyscanner.", cards.size());

            // 3. Iteración sobre cada tarjeta para extraer la información.
            for (Element card : cards) {
                // Extracción del título (destino)
                String titulo = card.select("h3.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--lg__YmYyY").text();

                // Extracción de la descripción (país y mes)
                String descripcion = card.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--sm__N2I5N").text();

                // Extracción del precio
                String precio = card.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--xs__ZjZkN").text()
                        .replace("Vuelos desde ", "S/ "); // Formateamos el precio

                // Extracción de la URL de la imagen
                // La imagen está en un 'div' con un 'background-image'.
                Element imageDiv = card.selectFirst("div.DestinationsCards_image__Y2Y4Z");
                String imagenUrl = "";
                if (imageDiv != null) {
                    // Extraemos la URL de la propiedad 'style'
                    String style = imageDiv.attr("style");
                    if (style.contains("url(")) {
                        int start = style.indexOf("url(") + 4;
                        int end = style.indexOf(")");
                        imagenUrl = style.substring(start, end).replace("\"", "");
                    }
                }

                // Si no se extrajo información clave, saltamos esta tarjeta.
                if (titulo.isEmpty() || precio.isEmpty()) {
                    continue;
                }

                // 4. Creación del objeto Oferta y adición a la lista.
                ofertas.add(new Oferta(
                        titulo,
                        descripcion,
                        precio,
                        "Vuelo", // Asumimos que son vuelos
                        imagenUrl,
                        "/vuelos", // Redirigimos a nuestra sección de vuelos
                        "Popular",
                        "tag-familiar" // Clase CSS de ejemplo para el tag
                ));
            }

        } catch (IOException e) {
            logger.error("Error durante el web scraping a {}: {}", SCRAPING_URL, e.getMessage());
            // Si el scraping falla, podríamos devolver una lista vacía o datos de respaldo.
            // Por ahora, devolvemos la lista vacía.
        }

        // Si no se encontraron ofertas, podemos devolver las de ejemplo para que la página no se vea vacía.
        if (ofertas.isEmpty()) {
            logger.warn("El scraping no devolvió resultados. Usando datos de respaldo.");
            return getFallbackOfertas();
        }

        return ofertas;
    }

    /**
     * Devuelve una lista de ofertas de ejemplo en caso de que el scraping falle.
     * @return Lista de ofertas de respaldo.
     */
    private List<Oferta> getFallbackOfertas() {
        List<Oferta> fallback = new ArrayList<>();
        fallback.add(new Oferta(
                "Vuelo a Cusco Mágico",
                "Descubre la capital del Imperio Inca. Vuelo directo desde Lima.",
                "S/ 250.00", "Vuelo", "/img/oferta-cusco.jpg", "/vuelos", "Top Ventas", "tag-top"
        ));
        fallback.add(new Oferta(
                "Crucero por el Caribe",
                "7 días de sol, playa y diversión en las islas más paradisíacas.",
                "S/ 3,500.00", "Crucero", "/img/oferta-caribe.jpg", "/cruceros", "Familiar", "tag-familiar"
        ));
        return fallback;
    }
}