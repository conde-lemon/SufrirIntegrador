package com.travel4u.demo;

import com.amadeus.resources.FlightOfferSearch;
import com.travel4u.demo.scraper.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integración para FlightService.
 * Esta prueba levanta el contexto de Spring y se conecta realmente a la API de Amadeus.
 * Asegúrate de tener tus credenciales correctas en application.properties.
 */
@SpringBootTest
class FlightServiceTest {

    @Autowired
    private FlightService flightService; // Inyectamos el servicio que queremos probar

    @Test
    void testBuscarVuelos_ConexionExitosa() {
        // Arrange: Preparamos los datos de prueba
        String origen = "MAD"; // Madrid
        String destino = "LHR"; // Londres Heathrow
        String fechaSalida = "2025-12-25"; // Una fecha futura
        int adultos = 1;

        // Act & Assert: Ejecutamos el método y verificamos el resultado
        assertDoesNotThrow(() -> {
            // Llamamos al método que se conecta a la API de Amadeus
            FlightOfferSearch[] resultados = flightService.buscarVuelos(origen, destino, fechaSalida, adultos);

            // Verificaciones para confirmar que la conexión fue exitosa
            assertNotNull(resultados, "La respuesta de Amadeus no debería ser nula.");

            // Opcional: Si esperamos resultados para esta ruta, podemos verificar que no esté vacía.
            // assertTrue(resultados.length > 0, "Se esperaba encontrar al menos una oferta de vuelo.");

            System.out.println("¡Conexión exitosa! Se encontraron " + resultados.length + " ofertas de vuelo.");

        }, "La llamada a la API de Amadeus no debería lanzar una ResponseException con credenciales válidas.");
    }
}