package com.travel4u.demo.controller;

import com.travel4u.demo.scraper.service.AmadeusDataExtractorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/amadeus-data")
public class AmadeusDataController {

    private final AmadeusDataExtractorService extractorService;

    public AmadeusDataController(AmadeusDataExtractorService extractorService) {
        this.extractorService = extractorService;
    }

    @PostMapping("/extraer-promociones")
    public ResponseEntity<Map<String, String>> extraerDatosPromociones() {
        try {
            extractorService.extraerDatosParaPromociones();
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Datos extraídos exitosamente. Revisa la carpeta 'amadeus_data' en el directorio del proyecto."
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Error al extraer datos: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/extraer-ruta")
    public ResponseEntity<String> extraerRutaEspecifica(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam String fecha) {
        
        String jsonData = extractorService.extraerDatosRutaEspecifica(origen, destino, fecha);
        return ResponseEntity.ok(jsonData);
    }

    @PostMapping("/ejecutar-extraccion")
    public ResponseEntity<Map<String, String>> ejecutarExtraccionCompleta() {
        try {
            extractorService.extraerDatosParaPromociones();
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Extracción ejecutada. Datos guardados en BD y archivos generados."
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Error: " + e.getMessage()
            ));
        }
    }
}