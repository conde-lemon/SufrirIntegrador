package com.travel4u.demo.reserva.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class FlightInfoGeneratorService {

    private final Random random = new Random();
    
    private final String[] GATES = {"A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10",
                                   "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10",
                                   "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10"};
    
    private final String[] BUS_PLATFORMS = {"Plataforma 1", "Plataforma 2", "Plataforma 3", "Plataforma 4", "Plataforma 5"};
    
    private final String[] CRUISE_TERMINALS = {"Terminal A", "Terminal B", "Terminal C", "Muelle 1", "Muelle 2"};

    public String generateBoardingGate(String tipoServicio) {
        if (tipoServicio == null) return generateFlightGate();
        
        return switch (tipoServicio.toLowerCase()) {
            case "vuelo", "flight" -> generateFlightGate();
            case "bus" -> generateBusPlatform();
            case "crucero", "cruise" -> generateCruiseTerminal();
            default -> generateFlightGate();
        };
    }

    public LocalDate generateDepartureDate() {
        // Generar fecha entre hoy y 30 días en el futuro
        LocalDate today = LocalDate.now();
        int daysToAdd = random.nextInt(30) + 1;
        return today.plusDays(daysToAdd);
    }

    public String generateDepartureTime(String tipoServicio) {
        if (tipoServicio == null) return generateFlightTime();
        
        return switch (tipoServicio.toLowerCase()) {
            case "vuelo", "flight" -> generateFlightTime();
            case "bus" -> generateBusTime();
            case "crucero", "cruise" -> generateCruiseTime();
            default -> generateFlightTime();
        };
    }

    private String generateFlightGate() {
        return GATES[random.nextInt(GATES.length)];
    }

    private String generateBusPlatform() {
        return BUS_PLATFORMS[random.nextInt(BUS_PLATFORMS.length)];
    }

    private String generateCruiseTerminal() {
        return CRUISE_TERMINALS[random.nextInt(CRUISE_TERMINALS.length)];
    }

    private String generateFlightTime() {
        // Vuelos: 6:00 AM - 11:00 PM
        int hour = random.nextInt(17) + 6; // 6-22
        int minute = random.nextInt(4) * 15; // 0, 15, 30, 45
        return LocalTime.of(hour, minute).format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String generateBusTime() {
        // Buses: 5:00 AM - 11:30 PM
        int hour = random.nextInt(19) + 5; // 5-23
        int minute = random.nextInt(4) * 15; // 0, 15, 30, 45
        return LocalTime.of(hour, minute).format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String generateCruiseTime() {
        // Cruceros: 8:00 AM - 6:00 PM
        int hour = random.nextInt(11) + 8; // 8-18
        int minute = 0; // Cruceros salen en punto
        return LocalTime.of(hour, minute).format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}