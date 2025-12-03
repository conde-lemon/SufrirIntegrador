package com.travel4u.demo.scraper.service;

import com.travel4u.demo.scraper.model.OfertaScraping;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test unitario para ScrapingService
 * Prueba la funcionalidad de scraping sin hacer llamadas reales a sitios web
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de ScrapingService")
class ScrapingServiceTest {

    @InjectMocks
    private ScrapingService scrapingService;

    private Document mockDocument;
    private Elements mockElements;

    @BeforeEach
    void setUp() {
        mockDocument = mock(Document.class);
        mockElements = new Elements();
    }

    @Test
    @DisplayName("Debería retornar ofertas de fallback cuando el scraping falla")
    void testScrapeOfertasPrincipales_WhenScrapingFails_ShouldReturnFallback() throws IOException {
        // Given: Jsoup.connect lanza IOException
        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);
            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenThrow(new IOException("Connection failed"));

            // When
            List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

            // Then
            assertNotNull(ofertas, "La lista de ofertas no debería ser nula");
            assertFalse(ofertas.isEmpty(), "Debería retornar ofertas de fallback");
            assertTrue(ofertas.size() >= 2, "Debería tener al menos 2 ofertas de fallback");

            // Verificar que las ofertas de fallback tienen datos válidos
            OfertaScraping primeraOferta = ofertas.getFirst();
            assertNotNull(primeraOferta.getTitulo(), "El título no debería ser nulo");
            assertNotNull(primeraOferta.getPrecio(), "El precio no debería ser nulo");
            assertNotNull(primeraOferta.getTipo(), "El tipo no debería ser nulo");
        }
    }

    @Test
    @DisplayName("Debería retornar fallback cuando no se encuentran elementos HTML")
    void testScrapeOfertasPrincipales_WhenNoElementsFound_ShouldReturnFallback() throws IOException {
        // Given: Documento sin elementos
        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);
            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(mockDocument);
            when(mockDocument.select(anyString())).thenReturn(new Elements());

            // When
            List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

            // Then
            assertNotNull(ofertas);
            assertFalse(ofertas.isEmpty(), "Debería retornar ofertas de fallback cuando no hay elementos");
        }
    }

    @Test
    @DisplayName("Debería procesar correctamente las ofertas cuando el scraping es exitoso")
    void testScrapeOfertasPrincipales_WhenScrapingSuccessful_ShouldReturnOffers() throws IOException {
        // Given: Mock de elementos HTML
        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);
            Element mockCard = mock(Element.class);
            Element mockTitle = mock(Element.class);
            Element mockDescription = mock(Element.class);
            Element mockPrice = mock(Element.class);
            Element mockImageDiv = mock(Element.class);

            mockElements.add(mockCard);

            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(mockDocument);
            when(mockDocument.select("a.BpkLink_bpk-link__MWZlZ.DestinationsCards_card__YmQ4M"))
                .thenReturn(mockElements);

            // Mock de la extracción de datos
            when(mockCard.select("h3.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--lg__YmYyY"))
                .thenReturn(new Elements(mockTitle));
            when(mockTitle.text()).thenReturn("Cusco");

            when(mockCard.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--sm__N2I5N"))
                .thenReturn(new Elements(mockDescription));
            when(mockDescription.text()).thenReturn("Perú • Diciembre");

            when(mockCard.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--xs__ZjZkN"))
                .thenReturn(new Elements(mockPrice));
            when(mockPrice.text()).thenReturn("Vuelos desde 250");

            when(mockCard.selectFirst("div.DestinationsCards_image__Y2Y4Z"))
                .thenReturn(mockImageDiv);
            when(mockImageDiv.attr("style"))
                .thenReturn("background-image: url(\"https://example.com/image.jpg\")");

            // When
            List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

            // Then
            assertNotNull(ofertas);
            assertFalse(ofertas.isEmpty());

            OfertaScraping oferta = ofertas.getFirst();
            assertEquals("Cusco", oferta.getTitulo());
            assertTrue(oferta.getPrecio().contains("S/"));
            assertEquals("Vuelo", oferta.getTipo());
        }
    }

    @Test
    @DisplayName("Debería omitir tarjetas sin título o precio")
    void testScrapeOfertasPrincipales_ShouldSkipCardsWithMissingData() throws IOException {
        // Given: Elementos sin datos completos
        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);
            Element mockCard = mock(Element.class);
            Elements emptyElements = new Elements();

            mockElements.add(mockCard);

            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(mockDocument);
            when(mockDocument.select(anyString())).thenReturn(mockElements);

            // Mock de elementos vacíos
            when(mockCard.select(anyString())).thenReturn(emptyElements);
            when(mockCard.selectFirst(anyString())).thenReturn(null);

            // When
            List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

            // Then
            assertNotNull(ofertas);
            assertFalse(ofertas.isEmpty(), "Debería retornar ofertas de fallback");
        }
    }

    @Test
    @DisplayName("Debería manejar correctamente la extracción de URLs de imágenes")
    void testImageUrlExtraction() throws IOException {
        // Given
        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);
            Element mockCard = mock(Element.class);
            Element mockTitle = mock(Element.class);
            Element mockPrice = mock(Element.class);
            Element mockImageDiv = mock(Element.class);

            mockElements.add(mockCard);

            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(mockDocument);
            when(mockDocument.select(anyString())).thenReturn(mockElements);

            when(mockCard.select("h3.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--lg__YmYyY"))
                .thenReturn(new Elements(mockTitle));
            when(mockTitle.text()).thenReturn("Lima");

            when(mockCard.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--xs__ZjZkN"))
                .thenReturn(new Elements(mockPrice));
            when(mockPrice.text()).thenReturn("Vuelos desde 150");

            when(mockCard.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--sm__N2I5N"))
                .thenReturn(new Elements());

            when(mockCard.selectFirst("div.DestinationsCards_image__Y2Y4Z"))
                .thenReturn(mockImageDiv);
            when(mockImageDiv.attr("style"))
                .thenReturn("background-image: url(\"https://images.example.com/lima.jpg\"); background-size: cover;");

            // When
            List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

            // Then
            assertNotNull(ofertas);
            assertFalse(ofertas.isEmpty());
            OfertaScraping oferta = ofertas.getFirst();
            assertTrue(oferta.getImagenUrl().contains("images.example.com"));
        }
    }

    @Test
    @DisplayName("Debería formatear correctamente el precio")
    void testPriceFormatting() throws IOException {
        // Given
        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);
            Element mockCard = mock(Element.class);
            Element mockTitle = mock(Element.class);
            Element mockPrice = mock(Element.class);

            mockElements.add(mockCard);

            jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(mockDocument);
            when(mockDocument.select(anyString())).thenReturn(mockElements);

            when(mockCard.select("h3.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--lg__YmYyY"))
                .thenReturn(new Elements(mockTitle));
            when(mockTitle.text()).thenReturn("Arequipa");

            when(mockCard.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--xs__ZjZkN"))
                .thenReturn(new Elements(mockPrice));
            when(mockPrice.text()).thenReturn("Vuelos desde 180");

            when(mockCard.select("p.BpkText_bpk-text__ZWIzZ.BpkText_bpk-text--sm__N2I5N"))
                .thenReturn(new Elements());
            when(mockCard.selectFirst(anyString())).thenReturn(null);

            // When
            List<OfertaScraping> ofertas = scrapingService.scrapeOfertasPrincipales();

            // Then
            assertNotNull(ofertas);
            assertFalse(ofertas.isEmpty());
            OfertaScraping oferta = ofertas.getFirst();
            assertTrue(oferta.getPrecio().startsWith("S/"), "El precio debería comenzar con S/");
            assertFalse(oferta.getPrecio().contains("Vuelos desde"),
                "El precio no debería contener 'Vuelos desde'");
        }
    }
}

