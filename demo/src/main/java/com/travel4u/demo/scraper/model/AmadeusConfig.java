package com.travel4u.demo.scraper.model;

import com.amadeus.Amadeus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusConfig {

    @Value("${amadeus.api-key}")
    private String apiKey;

    @Value("${amadeus.api-secret}")
    private String apiSecret;

    @Value("${amadeus.environment:test}")
    private String environment;

    @Bean
    public Amadeus amadeusClient() {
        if ("production".equalsIgnoreCase(environment)) {
            return Amadeus.builder(apiKey, apiSecret).build();
        } else {
            return Amadeus.builder(apiKey, apiSecret).build(); // test env by default
        }
    }
}
