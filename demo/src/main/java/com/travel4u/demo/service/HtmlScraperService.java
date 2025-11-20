package com.travel4u.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HtmlScraperService {

    private static final Logger logger = LoggerFactory.getLogger(HtmlScraperService.class);
    
    @Value("${scraper.html.auto:false}")
    private boolean autoScrapingEnabled;
    
    private boolean hasExecuted = false;

    // @EventListener(ApplicationReadyEvent.class) - DESHABILITADO
    public void scrapeTrivagoHtmlAuto() {
        // Método deshabilitado para evitar ejecución automática
        logger.info("Scraper HTML automático completamente deshabilitado");
    }
    
    public void scrapeTrivagoHtml() {
        if (hasExecuted) {
            logger.info("Scraper ya fue ejecutado anteriormente");
            return;
        }
        
        hasExecuted = true;
        logger.info("=== INICIANDO SCRAPER DE HTML TRIVAGO ===");
        
        try {
            // Leer el archivo HTML descargado
            ClassPathResource resource = new ClassPathResource("scraping/TRIVAGO.html");
            
            if (!resource.exists()) {
                logger.error("Archivo TRIVAGO.html no encontrado en resources/scraping/");
                return;
            }
            
            File htmlFile = resource.getFile();
            logger.info("Leyendo archivo: {}", htmlFile.getAbsolutePath());
            
            // Parsear el HTML con Jsoup
            Document doc = Jsoup.parse(htmlFile, "UTF-8");
            
            // Extraer información de hoteles
            List<Map<String, Object>> hotels = extractHotelData(doc);
            
            // Guardar en JSON
            saveToJson(hotels);
            
            logger.info("=== SCRAPING COMPLETADO ===");
            logger.info("Hoteles extraídos: {}", hotels.size());
            
        } catch (Exception e) {
            logger.error("Error al procesar el archivo HTML: ", e);
        }
    }
    
    private List<Map<String, Object>> extractHotelData(Document doc) {
        List<Map<String, Object>> hotels = new ArrayList<>();
        
        // Buscar elementos de hoteles usando diferentes selectores
        Elements hotelElements = doc.select("article[data-testid='item']");
        
        if (hotelElements.isEmpty()) {
            // Intentar otros selectores comunes
            hotelElements = doc.select(".accommodation-item, .hotel-item, [data-qa='hotel-item']");
        }
        
        logger.info("Elementos de hotel encontrados: {}", hotelElements.size());
        
        for (Element hotel : hotelElements) {
            Map<String, Object> hotelData = new HashMap<>();
            
            // Extraer nombre
            String name = extractText(hotel, "[data-testid='item-name'], .hotel-name, h3, h4");
            hotelData.put("name", name);
            
            // Extraer imagen con múltiples selectores
            String image = extractAttribute(hotel, "img[data-testid='accommodation-main-image'], img[data-testid='item-image'], .accommodation-image img, img", "src");
            // Si no hay imagen, usar data-src o srcset
            if (image.isEmpty()) {
                image = extractAttribute(hotel, "img", "data-src");
            }
            if (image.isEmpty()) {
                String srcset = extractAttribute(hotel, "img", "srcset");
                if (!srcset.isEmpty()) {
                    // Extraer la primera URL del srcset
                    image = srcset.split(",")[0].split(" ")[0];
                }
            }
            hotelData.put("image", image);
            
            // Extraer rating
            String rating = extractText(hotel, "[data-testid='aggregate-rating'] span[itemprop='ratingValue'], .rating, .score");
            hotelData.put("rating", rating);
            
            // Extraer precio
            String price = extractText(hotel, "[data-testid='recommended-price'], .price, .rate");
            hotelData.put("price", price);
            
            // Extraer tipo de alojamiento
            String hotelType = extractText(hotel, "[data-testid='accommodation-type'], .accommodation-type");
            hotelData.put("hotelType", hotelType);
            
            // Extraer distancia
            String distance = extractText(hotel, "[data-testid='distance-label-section'], .distance");
            hotelData.put("distance", distance);
            
            // Extraer características destacadas
            String highlights = extractText(hotel, "[data-testid='hotel-highlights-text'], .highlights, .amenities");
            hotelData.put("highlights", highlights);
            
            // Contar estrellas
            int stars = hotel.select("[data-testid='star'], .star, .rating-star").size();
            hotelData.put("stars", stars);
            
            // Solo agregar si tiene nombre
            if (name != null && !name.trim().isEmpty()) {
                hotels.add(hotelData);
                logger.debug("Hotel extraído: {}", name);
            }
        }
        
        return hotels;
    }
    
    private String extractText(Element parent, String selector) {
        Elements elements = parent.select(selector);
        return elements.isEmpty() ? "" : elements.first().text().trim();
    }
    
    private String extractAttribute(Element parent, String selector, String attribute) {
        Elements elements = parent.select(selector);
        return elements.isEmpty() ? "" : elements.first().attr(attribute);
    }
    
    private void saveToJson(List<Map<String, Object>> hotels) {
        try {
            ClassPathResource resource = new ClassPathResource("scraping");
            File scraperDir = resource.getFile();
            File jsonFile = new File(scraperDir, "hoteles_tacna.json");
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, hotels);
            
            logger.info("Datos guardados en: {}", jsonFile.getAbsolutePath());
            
        } catch (IOException e) {
            logger.error("Error al guardar JSON: ", e);
        }
    }
}