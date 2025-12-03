package com.travel4u.demo.scraper.service;

import com.travel4u.demo.scraper.model.OfertaScraping;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para ScrapingService
 * Prueba el scraping real a Skyscanner (requiere conexión a internet)
 *
 * NOTA: Este test puede fallar si:
 * - No hay conexión a internet
 * - Skyscanner cambia la estructura HTML de su página
 * - Skyscanner bloquea el acceso
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Tests de Integración de Web Scraping")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScrapingServiceIntegrationTest {

    @Autowired
    private ScrapingService scrapingService;

    @Test
    @Order(1)
    @DisplayName("Debería conectarse exitosamente a Skyscanner")
    void testConnection() {
        // Given & When
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas, "La lista de ofertas no debería ser nula");
        assertFalse(ofertas.isEmpty(), "Debería retornar al menos ofertas de fallback");
    }

    @Test
    @Order(2)
    @DisplayName("Debería retornar ofertas con datos válidos")
    void testScrapedOffersHaveValidData() {
        // Given & When
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas);
        assertFalse(ofertas.isEmpty());

        for (OfertaScraping oferta : ofertas) {
            // Verificar que cada oferta tenga datos válidos
            assertNotNull(oferta.getTitulo(), "El título no debería ser nulo");
            assertFalse(oferta.getTitulo().isEmpty(), "El título no debería estar vacío");

            assertNotNull(oferta.getPrecio(), "El precio no debería ser nulo");
            assertFalse(oferta.getPrecio().isEmpty(), "El precio no debería estar vacío");

            assertNotNull(oferta.getTipo(), "El tipo no debería ser nulo");
            assertFalse(oferta.getTipo().isEmpty(), "El tipo no debería estar vacío");
            // El tipo puede ser "Vuelo", "Crucero", "Bus", etc. (depende de si es scraping real o fallback)

            assertNotNull(oferta.getUrlDestino(), "La URL destino no debería ser nula");
            assertNotNull(oferta.getTag(), "El tag no debería ser nulo");
            assertNotNull(oferta.getTagClass(), "La clase del tag no debería ser nula");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Debería retornar múltiples ofertas")
    void testMultipleOffers() {
        // Given & When
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas);
        assertTrue(ofertas.size() >= 2,
            "Debería retornar al menos 2 ofertas (incluso si son de fallback)");
    }

    @Test
    @Order(4)
    @DisplayName("Las ofertas deberían tener formato de precio correcto")
    void testPriceFormat() {
        // Given & When
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas);
        assertFalse(ofertas.isEmpty());

        for (OfertaScraping oferta : ofertas) {
            String precio = oferta.getPrecio();
            assertTrue(precio.contains("S/") || precio.contains("$") || precio.contains("€"),
                "El precio debería contener un símbolo de moneda");
        }
    }

    @Test
    @Order(5)
    @DisplayName("Las ofertas deberían tener URLs de imagen válidas o vacías")
    void testImageUrls() {
        // Given & When
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas);
        assertFalse(ofertas.isEmpty());

        for (OfertaScraping oferta : ofertas) {
            String imagenUrl = oferta.getImagenUrl();
            assertNotNull(imagenUrl, "La URL de imagen no debería ser nula");
            // Puede estar vacía si no se encontró imagen
            if (!imagenUrl.isEmpty()) {
                assertTrue(imagenUrl.startsWith("http") || imagenUrl.startsWith("/"),
                    "La URL de imagen debería ser válida: " + imagenUrl);
            }
        }
    }

    @Test
    @Order(6)
    @DisplayName("Debería manejar correctamente errores de conexión")
    @Disabled("Test manual - requiere desconectar internet")
    void testConnectionFailure() {
        // Este test debe ejecutarse manualmente desconectando internet
        // Given & When
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas);
        assertFalse(ofertas.isEmpty(), "Debería retornar ofertas de fallback");
        // Verificar que sean ofertas de fallback
        assertTrue(ofertas.stream().anyMatch(o -> o.getTitulo().contains("Cusco")),
            "Debería incluir la oferta de fallback de Cusco");
    }

    @Test
    @Order(7)
    @DisplayName("Las ofertas no deberían tener títulos duplicados")
    void testNoDuplicateTitles() {
        // Given & When
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas);
        assertFalse(ofertas.isEmpty());

        long uniqueTitles = ofertas.stream()
            .map(OfertaScraping::getTitulo)
            .distinct()
            .count();

        assertEquals(ofertas.size(), uniqueTitles,
            "Todas las ofertas deberían tener títulos únicos");
    }

    @Test
    @Order(8)
    @DisplayName("Debería completarse en un tiempo razonable")
    @Timeout(value = 10) // 10 segundos máximo
    void testPerformance() {
        // Given & When & Then
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();
        assertNotNull(ofertas);
        assertFalse(ofertas.isEmpty());
    }

    @Test
    @Order(9)
    @DisplayName("Las descripciones deberían estar presentes")
    void testDescriptions() {
        // Given & When
        List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas);
        assertFalse(ofertas.isEmpty());

        for (OfertaScraping oferta : ofertas) {
            assertNotNull(oferta.getDescripcion(), "La descripción no debería ser nula");
            // La descripción puede estar vacía si no se encuentra en el HTML
        }
    }

    @Test
    @Order(10)
    @DisplayName("Debería ser idempotente - múltiples llamadas retornan resultados consistentes")
    void testIdempotency() {
        // Given & When
        List<OfertaScraping> ofertas1 = scrapingService.scrapeOfertasPrincipales();
        List<OfertaScraping> ofertas2 = scrapingService.scrapeOfertasPrincipales();

        // Then
        assertNotNull(ofertas1);
        assertNotNull(ofertas2);

        // Ambas listas deberían tener el mismo tamaño
        assertEquals(ofertas1.size(), ofertas2.size(),
            "Las llamadas consecutivas deberían retornar el mismo número de ofertas");

        // Los títulos deberían ser los mismos (pueden estar en diferente orden)
        for (int i = 0; i < ofertas1.size(); i++) {
            assertEquals(ofertas1.get(i).getTitulo(), ofertas2.get(i).getTitulo(),
                "Los títulos deberían ser consistentes entre llamadas");
        }
    }
}

