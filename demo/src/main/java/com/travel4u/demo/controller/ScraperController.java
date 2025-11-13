package com.travel4u.demo.controller;

import com.travel4u.demo.service.NodeScraperService;
import com.travel4u.demo.service.HtmlScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scraper")
public class ScraperController {

    private static final Logger logger = LoggerFactory.getLogger(ScraperController.class);

    @Autowired
    private NodeScraperService nodeScraperService;
    
    @Autowired
    private HtmlScraperService htmlScraperService;

    @PostMapping("/execute")
    public ResponseEntity<Map<String, String>> executeScraper() {
        logger.info("Ejecutando scraper manualmente via API");
        
        Map<String, String> response = new HashMap<>();
        
        try {
            // Ejecutar ambos scrapers en un hilo separado
            Thread scraperThread = new Thread(() -> {
                htmlScraperService.scrapeTrivagoHtml();
                nodeScraperService.executeNodeScraper();
            });
            scraperThread.start();
            
            response.put("status", "success");
            response.put("message", "Scraper iniciado correctamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al ejecutar scraper: ", e);
            response.put("status", "error");
            response.put("message", "Error al iniciar el scraper: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getScraperStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "available");
        response.put("message", "Scraper service is running");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/test")
    public String getTestPage() {
        return "scraper-test";
    }
}