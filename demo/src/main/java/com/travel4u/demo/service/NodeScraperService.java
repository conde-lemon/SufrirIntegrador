package com.travel4u.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class NodeScraperService {

    private static final Logger logger = LoggerFactory.getLogger(NodeScraperService.class);
    
    @Value("${scraper.node.enabled:false}")
    private boolean scraperEnabled;

    @EventListener(ApplicationReadyEvent.class)
    public void executeNodeScraperOnStartup() {
        executeNodeScraper();
    }
    
    public void executeNodeScraper() {
        if (!scraperEnabled) {
            logger.info("Scraper de Node.js deshabilitado en configuración");
            return;
        }
        logger.info("=== INICIANDO SCRAPER DE NODE.JS ===");
        
        try {
            // Obtener la ruta del directorio de scraping
            ClassPathResource resource = new ClassPathResource("scraping");
            String scraperPath;
            
            if (resource.exists()) {
                scraperPath = resource.getFile().getAbsolutePath();
                logger.info("Directorio de scraping: {}", scraperPath);
            } else {
                logger.error("Directorio de scraping no encontrado en resources");
                return;
            }
            
            // Verificar si Node.js está instalado
            if (!isNodeInstalled()) {
                logger.warn("Node.js no está instalado. Saltando ejecución del scraper.");
                return;
            }
            
            // Verificar si las dependencias están instaladas
            File nodeModules = new File(scraperPath, "node_modules");
            if (!nodeModules.exists()) {
                logger.info("Instalando dependencias de Node.js...");
                installNodeDependencies(scraperPath);
            }
            
            // Ejecutar el scraper
            executeScraperScript(scraperPath);
            
        } catch (Exception e) {
            logger.error("Error al ejecutar el scraper de Node.js: ", e);
        }
    }
    
    private boolean isNodeInstalled() {
        try {
            ProcessBuilder pb = new ProcessBuilder("node", "--version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    private void installNodeDependencies(String scraperPath) {
        try {
            logger.info("Ejecutando: npm install");
            ProcessBuilder pb = new ProcessBuilder("npm", "install");
            pb.directory(new File(scraperPath));
            pb.inheritIO(); // Para ver la salida en consola
            
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                logger.info("Dependencias instaladas correctamente");
            } else {
                logger.error("Error al instalar dependencias. Código de salida: {}", exitCode);
            }
        } catch (Exception e) {
            logger.error("Error al instalar dependencias: ", e);
        }
    }
    
    private void executeScraperScript(String scraperPath) {
        try {
            logger.info("Ejecutando scraper de Trivago...");
            ProcessBuilder pb = new ProcessBuilder("node", "trivagoScraper.js");
            pb.directory(new File(scraperPath));
            pb.inheritIO(); // Para ver la salida en consola
            
            Process process = pb.start();
            
            // Ejecutar en un hilo separado para no bloquear la aplicación
            Thread scraperThread = new Thread(() -> {
                try {
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        logger.info("Scraper ejecutado correctamente");
                    } else {
                        logger.error("Error en el scraper. Código de salida: {}", exitCode);
                    }
                } catch (InterruptedException e) {
                    logger.error("Scraper interrumpido: ", e);
                    Thread.currentThread().interrupt();
                }
            });
            
            scraperThread.setName("TrivagoCraper-Thread");
            scraperThread.start();
            
        } catch (Exception e) {
            logger.error("Error al ejecutar el scraper: ", e);
        }
    }
}