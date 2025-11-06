package com.travel4u.demo.scraper.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.travel4u.demo.scraper.model.OfertaScraping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);
    private static final int MAX_RESULTS = 5;
    private static final int DEFAULT_ADULTS = 1;
    
    private final Amadeus amadeus;
    
    @Value("${flight.default.origin:LIM}")
    private String defaultOrigin;
    
    @Value("${flight.default.destination:MAD}")
    private String defaultDestination;

    public FlightService(Amadeus amadeus) {
        this.amadeus = amadeus;
    }

    /**
     * Busca ofertas de vuelos en Amadeus y devuelve los datos crudos de la API.
     */
    public FlightOfferSearch[] buscarVuelos(String origen, String destino, String fechaSalida, int adultos) throws ResponseException {
        validateInputs(origen, destino, fechaSalida, adultos);
        
        logger.debug("Buscando vuelos: {} -> {}, fecha: {}, adultos: {}", origen, destino, fechaSalida, adultos);
        
        return amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origen.toUpperCase())
                        .and("destinationLocationCode", destino.toUpperCase())
                        .and("departureDate", fechaSalida)
                        .and("adults", adultos)
                        .and("max", MAX_RESULTS)
        );
    }

    /**
     * Busca ofertas de vuelos y las transforma en una lista de objetos 'Oferta' para mostrar en el frontend.
     * @return Una lista de Ofertas lista para ser renderizada.
     */
    public List<OfertaScraping> buscarOfertasDeVuelosParaIndex() {
        List<OfertaScraping> ofertasTransformadas = new ArrayList<>();
        
        try {
            String fecha = LocalDate.now().plusDays(30).format(DateTimeFormatter.ISO_LOCAL_DATE);
            FlightOfferSearch[] flightOffers = buscarVuelos(defaultOrigin, defaultDestination, fecha, DEFAULT_ADULTS);

            logger.info("Se obtuvieron {} ofertas de vuelo desde la API de Amadeus.", flightOffers.length);

            for (FlightOfferSearch flightOffer : flightOffers) {
                OfertaScraping oferta = transformarOferta(flightOffer, defaultOrigin, defaultDestination);
                if (oferta != null) {
                    ofertasTransformadas.add(oferta);
                }
            }

        } catch (ResponseException e) {
            logger.warn("Error al conectar con la API de Amadeus: {}. Código: {}", e.getMessage(), e.getCode());
        } catch (Exception e) {
            logger.error("Error inesperado al procesar ofertas de vuelos", e);
        }
        
        return ofertasTransformadas;
    }
    
    private void validateInputs(String origen, String destino, String fechaSalida, int adultos) {
        if (origen == null || origen.trim().length() != 3) {
            throw new IllegalArgumentException("Código de origen debe tener 3 caracteres");
        }
        if (destino == null || destino.trim().length() != 3) {
            throw new IllegalArgumentException("Código de destino debe tener 3 caracteres");
        }
        if (fechaSalida == null || fechaSalida.trim().isEmpty()) {
            throw new IllegalArgumentException("Fecha de salida es requerida");
        }
        if (adultos < 1 || adultos > 9) {
            throw new IllegalArgumentException("Número de adultos debe estar entre 1 y 9");
        }
    }
    
    private OfertaScraping transformarOferta(FlightOfferSearch flightOffer, String origen, String destino) {
        try {
            String titulo = String.format("Vuelo %s → %s", origen, destino);
            
            String aerolinea = (flightOffer.getValidatingAirlineCodes() != null && flightOffer.getValidatingAirlineCodes().length > 0) 
                ? flightOffer.getValidatingAirlineCodes()[0] : "Aerolínea";
            
            int escalas = flightOffer.getItineraries()[0].getSegments().length - 1;
            String descripcion = String.format("Aerolínea: %s. %s", aerolinea, 
                escalas == 0 ? "Vuelo directo" : escalas + " escala(s)");

            BigDecimal precioDecimal = new BigDecimal(flightOffer.getPrice().getTotal());
            String precio = String.format("S/ %.2f", precioDecimal);

            return new OfertaScraping(
                    titulo,
                    descripcion,
                    precio,
                    "Vuelo API",
                    "/img/vuelo-api.jpg",
                    "/vuelos",
                    "Oferta Real",
                    "tag-top"
            );
        } catch (Exception e) {
            logger.warn("Error al transformar oferta de vuelo: {}", e.getMessage());
            return null;
        }
    }
}