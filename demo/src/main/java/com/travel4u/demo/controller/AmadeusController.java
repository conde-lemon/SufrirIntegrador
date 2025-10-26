package com.travel4u.demo.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Hotel;

import com.travel4u.demo.scraper.service.FlightService;
import com.travel4u.demo.scraper.service.HotelService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/amadeus")
public class AmadeusController {

    private final FlightService flightService;
    private final HotelService hotelService;

    public AmadeusController(FlightService flightService, HotelService hotelService) {
        this.flightService = flightService;
        this.hotelService = hotelService;
    }

    @GetMapping("/vuelos")
    public FlightOfferSearch[] buscarVuelos(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam String fecha,
            @RequestParam(defaultValue = "1") int adultos) throws ResponseException {
        return flightService.buscarVuelos(origen, destino, fecha, adultos);
    }

    @GetMapping("/hoteles")
    public Hotel[] buscarHoteles(@RequestParam String ciudad) throws ResponseException {
        return hotelService.buscarHoteles(ciudad);
    }
}
