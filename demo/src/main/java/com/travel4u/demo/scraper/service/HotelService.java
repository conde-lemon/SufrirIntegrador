package com.travel4u.demo.scraper.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Hotel;

import org.springframework.stereotype.Service;

@Service
public class HotelService {

    private final Amadeus amadeus;

    public HotelService(Amadeus amadeus) {
        this.amadeus = amadeus;
    }

    public Hotel[] buscarHoteles(String codigoCiudad) throws ResponseException {
        return amadeus.referenceData.locations.hotels.byCity.get(
                Params.with("cityCode", codigoCiudad)
        );
    }
}
