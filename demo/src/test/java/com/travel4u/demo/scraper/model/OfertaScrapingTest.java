package com.travel4u.demo.scraper.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test para el modelo OfertaScraping
 * Verifica que el modelo funcione correctamente con Lombok
 */
@DisplayName("Tests del Modelo OfertaScraping")
class OfertaScrapingTest {

    @Test
    @DisplayName("Debería crear una oferta con todos los campos")
    void testConstructorWithAllFields() {
        // Given & When
        OfertaScraping oferta = new OfertaScraping(
            "Cusco Mágico",
            "Perú • Diciembre",
            "S/ 250.00",
            "Vuelo",
            "https://example.com/image.jpg",
            "/vuelos",
            "Popular",
            "tag-popular"
        );

        // Then
        assertEquals("Cusco Mágico", oferta.getTitulo());
        assertEquals("Perú • Diciembre", oferta.getDescripcion());
        assertEquals("S/ 250.00", oferta.getPrecio());
        assertEquals("Vuelo", oferta.getTipo());
        assertEquals("https://example.com/image.jpg", oferta.getImagenUrl());
        assertEquals("/vuelos", oferta.getUrlDestino());
        assertEquals("Popular", oferta.getTag());
        assertEquals("tag-popular", oferta.getTagClass());
    }

    @Test
    @DisplayName("Debería crear una oferta vacía con constructor sin argumentos")
    void testNoArgsConstructor() {
        // Given & When
        OfertaScraping oferta = new OfertaScraping();

        // Then
        assertNotNull(oferta);
        assertNull(oferta.getTitulo());
        assertNull(oferta.getDescripcion());
        assertNull(oferta.getPrecio());
        assertNull(oferta.getTipo());
        assertNull(oferta.getImagenUrl());
        assertNull(oferta.getUrlDestino());
        assertNull(oferta.getTag());
        assertNull(oferta.getTagClass());
    }

    @Test
    @DisplayName("Debería permitir modificar los campos con setters")
    void testSetters() {
        // Given
        OfertaScraping oferta = new OfertaScraping();

        // When
        oferta.setTitulo("Lima Colonial");
        oferta.setDescripcion("Perú • Enero");
        oferta.setPrecio("S/ 180.00");
        oferta.setTipo("Vuelo");
        oferta.setImagenUrl("https://example.com/lima.jpg");
        oferta.setUrlDestino("/destinos/lima");
        oferta.setTag("Top Ventas");
        oferta.setTagClass("tag-top");

        // Then
        assertEquals("Lima Colonial", oferta.getTitulo());
        assertEquals("Perú • Enero", oferta.getDescripcion());
        assertEquals("S/ 180.00", oferta.getPrecio());
        assertEquals("Vuelo", oferta.getTipo());
        assertEquals("https://example.com/lima.jpg", oferta.getImagenUrl());
        assertEquals("/destinos/lima", oferta.getUrlDestino());
        assertEquals("Top Ventas", oferta.getTag());
        assertEquals("tag-top", oferta.getTagClass());
    }

    @Test
    @DisplayName("Debería comparar correctamente dos ofertas iguales")
    void testEquals() {
        // Given
        OfertaScraping oferta1 = new OfertaScraping(
            "Cusco", "Perú", "S/ 250", "Vuelo",
            "img.jpg", "/vuelos", "Popular", "tag-popular"
        );
        OfertaScraping oferta2 = new OfertaScraping(
            "Cusco", "Perú", "S/ 250", "Vuelo",
            "img.jpg", "/vuelos", "Popular", "tag-popular"
        );

        // When & Then
        assertEquals(oferta1, oferta2, "Dos ofertas con los mismos datos deberían ser iguales");
        assertEquals(oferta1.hashCode(), oferta2.hashCode(),
            "Dos ofertas iguales deberían tener el mismo hashCode");
    }

    @Test
    @DisplayName("Debería distinguir ofertas diferentes")
    void testNotEquals() {
        // Given
        OfertaScraping oferta1 = new OfertaScraping(
            "Cusco", "Perú", "S/ 250", "Vuelo",
            "img.jpg", "/vuelos", "Popular", "tag-popular"
        );
        OfertaScraping oferta2 = new OfertaScraping(
            "Lima", "Perú", "S/ 180", "Vuelo",
            "img2.jpg", "/vuelos", "Top", "tag-top"
        );

        // When & Then
        assertNotEquals(oferta1, oferta2, "Ofertas con datos diferentes no deberían ser iguales");
    }

    @Test
    @DisplayName("Debería generar un toString legible")
    void testToString() {
        // Given
        OfertaScraping oferta = new OfertaScraping(
            "Arequipa", "Perú • Febrero", "S/ 200", "Vuelo",
            "arequipa.jpg", "/vuelos", "Familiar", "tag-familiar"
        );

        // When
        String toString = oferta.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Arequipa"), "toString debería contener el título");
        assertTrue(toString.contains("S/ 200"), "toString debería contener el precio");
        assertTrue(toString.contains("Vuelo"), "toString debería contener el tipo");
    }

    @Test
    @DisplayName("Debería manejar valores nulos sin lanzar excepciones")
    void testNullValues() {
        // Given & When
        OfertaScraping oferta = new OfertaScraping(
            null, null, null, null, null, null, null, null
        );

        // Then
        assertNotNull(oferta);
        assertNull(oferta.getTitulo());
        assertNull(oferta.getDescripcion());
        assertNull(oferta.getPrecio());
        assertNull(oferta.getTipo());
        assertNull(oferta.getImagenUrl());
        assertNull(oferta.getUrlDestino());
        assertNull(oferta.getTag());
        assertNull(oferta.getTagClass());
    }

    @Test
    @DisplayName("Debería manejar strings vacíos")
    void testEmptyStrings() {
        // Given & When
        OfertaScraping oferta = new OfertaScraping(
            "", "", "", "", "", "", "", ""
        );

        // Then
        assertEquals("", oferta.getTitulo());
        assertEquals("", oferta.getDescripcion());
        assertEquals("", oferta.getPrecio());
        assertEquals("", oferta.getTipo());
        assertEquals("", oferta.getImagenUrl());
        assertEquals("", oferta.getUrlDestino());
        assertEquals("", oferta.getTag());
        assertEquals("", oferta.getTagClass());
    }

    @Test
    @DisplayName("Debería manejar caracteres especiales en los campos")
    void testSpecialCharacters() {
        // Given & When
        OfertaScraping oferta = new OfertaScraping(
            "Cañón del Colca",
            "Perú • ¡Oferta! 🎉",
            "S/ 1,250.50",
            "Tour & Vuelo",
            "https://example.com/image?id=123&size=large",
            "/destinos/colca?promocion=true&descuento=20%",
            "¡Top Ventas!",
            "tag-top-ventas"
        );

        // Then
        assertEquals("Cañón del Colca", oferta.getTitulo());
        assertTrue(oferta.getDescripcion().contains("¡Oferta!"));
        assertTrue(oferta.getPrecio().contains(","));
        assertEquals("Tour & Vuelo", oferta.getTipo());
        assertTrue(oferta.getImagenUrl().contains("&"));
        assertTrue(oferta.getUrlDestino().contains("%"));
    }

    @Test
    @DisplayName("Debería validar que los tipos de oferta sean consistentes")
    void testOfferTypes() {
        // Given
        String[] tiposValidos = {"Vuelo", "Crucero", "Bus", "Hospedaje", "Tour", "Paquete"};

        // When & Then
        for (String tipo : tiposValidos) {
            OfertaScraping oferta = new OfertaScraping();
            oferta.setTipo(tipo);
            assertEquals(tipo, oferta.getTipo(),
                "El tipo " + tipo + " debería ser válido");
        }
    }

    @Test
    @DisplayName("Debería manejar precios con diferentes formatos")
    void testPriceFormats() {
        // Given
        String[] preciosValidos = {
            "S/ 250.00",
            "S/ 1,250.50",
            "$150.00",
            "€200",
            "Desde S/ 100"
        };

        // When & Then
        for (String precio : preciosValidos) {
            OfertaScraping oferta = new OfertaScraping();
            oferta.setPrecio(precio);
            assertEquals(precio, oferta.getPrecio(),
                "El precio " + precio + " debería ser válido");
        }
    }

    @Test
    @DisplayName("Debería manejar URLs de diferentes dominios")
    void testImageUrlFormats() {
        // Given
        String[] urlsValidas = {
            "https://example.com/image.jpg",
            "https://cdn.skyscanner.com/images/offer123.png",
            "/static/images/cusco.jpg",
            "//images.travel.com/destination.webp",
            "data:image/jpeg;base64,..."
        };

        // When & Then
        for (String url : urlsValidas) {
            OfertaScraping oferta = new OfertaScraping();
            oferta.setImagenUrl(url);
            assertEquals(url, oferta.getImagenUrl(),
                "La URL " + url + " debería ser válida");
        }
    }

    @Test
    @DisplayName("Debería permitir clonar ofertas modificando solo algunos campos")
    void testPartialUpdate() {
        // Given
        OfertaScraping original = new OfertaScraping(
            "Cusco", "Perú", "S/ 250", "Vuelo",
            "img.jpg", "/vuelos", "Popular", "tag-popular"
        );

        // When
        OfertaScraping modificada = new OfertaScraping(
            original.getTitulo(),
            original.getDescripcion(),
            "S/ 200", // Precio modificado
            original.getTipo(),
            original.getImagenUrl(),
            original.getUrlDestino(),
            "Oferta Especial", // Tag modificado
            "tag-oferta"
        );

        // Then
        assertEquals(original.getTitulo(), modificada.getTitulo());
        assertNotEquals(original.getPrecio(), modificada.getPrecio());
        assertNotEquals(original.getTag(), modificada.getTag());
    }
}

