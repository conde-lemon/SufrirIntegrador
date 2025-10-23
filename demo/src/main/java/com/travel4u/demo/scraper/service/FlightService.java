package com.travel4u.demo.scraper.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.travel4u.demo.scraper.model.Oferta; // Importa tu modelo Oferta
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);
    private final Amadeus amadeus;

    public FlightService(Amadeus amadeus) {
        this.amadeus = amadeus;
    }

    /**
     * Busca ofertas de vuelos en Amadeus y devuelve los datos crudos de la API.
     */
    public FlightOfferSearch[] buscarVuelos(String origen, String destino, String fechaSalida, int adultos) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origen)
                        .and("destinationLocationCode", destino)
                        .and("departureDate", fechaSalida)
                        .and("adults", adultos)
                        .and("max", 5) // Limita a 5 resultados para el index
        );
    }

    /**
     * NUEVO: Busca ofertas de vuelos y las transforma en una lista de objetos 'Oferta' para mostrar en el frontend.
     * @return Una lista de Ofertas lista para ser renderizada.
     */
    public List<Oferta> buscarOfertasDeVuelosParaIndex() {
        List<Oferta> ofertasTransformadas = new ArrayList<>();
        try {
            // Usamos valores por defecto para mostrar ofertas populares en el index
            String origen = "LHR"; // Londres
            String destino = "CDG"; // París
            String fecha = "2025-11-20";

            FlightOfferSearch[] flightOffers = buscarVuelos(origen, destino, fecha, 1);

            logger.info("Se obtuvieron {} ofertas de vuelo desde la API de Amadeus.", flightOffers.length);

            // Transformamos cada oferta de Amadeus a nuestro objeto 'Oferta'
            for (FlightOfferSearch flightOffer : flightOffers) {
                // Construimos un título y descripción significativos
                String titulo = "Vuelo de " + origen + " a " + destino;
                String descripcion = "Aerolínea: " + flightOffer.getValidatingAirlineCodes()[0] + ". " +
                        (flightOffer.getItineraries()[0].getSegments().length - 1) + " escalas.";

                // Formateamos el precio
                String precio = String.format("S/ %.2f", Double.parseDouble(flightOffer.getPrice().getTotal()));

                ofertasTransformadas.add(new Oferta(
                        titulo,
                        descripcion,
                        precio,
                        "Vuelo API", // Tipo para diferenciarlo
                        "/img/vuelo-api.jpg", // Imagen de placeholder
                        "/vuelos", // URL de destino
                        "Oferta Real",
                        "tag-top" // Clase CSS
                ));
            }

        } catch (ResponseException e) {
            logger.error("Error al conectar con la API de Amadeus: {}", e.getMessage());
            // Si hay un error, devolvemos una lista vacía. El controlador se encargará del fallback.
        }
        return ofertasTransformadas;
    }
}