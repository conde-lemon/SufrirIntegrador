package com.travel4u.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HotelOfferService {

    private static final Logger logger = LoggerFactory.getLogger(HotelOfferService.class);

    public List<Map<String, Object>> getHotelOffers() {
        try {
            ClassPathResource resource = new ClassPathResource("scraping/hoteles_tacna.json");
            
            if (!resource.exists()) {
                logger.warn("Archivo hoteles_tacna.json no encontrado");
                return new ArrayList<>();
            }
            
            File jsonFile = resource.getFile();
            ObjectMapper mapper = new ObjectMapper();
            
            List<Map<String, Object>> hotels = mapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>() {});
            
            logger.info("Hoteles cargados desde JSON: {}", hotels.size());
            return hotels;
            
        } catch (Exception e) {
            logger.error("Error al leer hoteles desde JSON: ", e);
            return new ArrayList<>();
        }
    }
}