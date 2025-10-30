package com.travel4u.demo.controller;

import com.travel4u.demo.ofertas.model.Oferta;
import com.travel4u.demo.ofertas.repository.IOfertasDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar Ofertas/Promociones
 * Base URL: /api/ofertas
 */
@RestController
@RequestMapping("/api/ofertas")
@CrossOrigin(origins = "*")
public class OfertaController {

    private final IOfertasDAO ofertasDAO;

    public OfertaController(IOfertasDAO ofertasDAO) {
        this.ofertasDAO = ofertasDAO;
    }

    /**
     * Obtener todas las ofertas
     * GET /api/ofertas
     */
    @GetMapping
    public ResponseEntity<List<Oferta>> getAllOfertas() {
        try {
            List<Oferta> ofertas = ofertasDAO.findAll();
            return ResponseEntity.ok(ofertas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener una oferta por ID
     * GET /api/ofertas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Oferta> getOfertaById(@PathVariable Long id) {
        try {
            Optional<Oferta> oferta = ofertasDAO.findById(id);
            return oferta.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crear nueva oferta/promoción
     * POST /api/ofertas
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOferta(@RequestBody Oferta oferta) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validaciones básicas
            if (oferta.getNombre() == null || oferta.getNombre().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El nombre de la promoción es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            if (oferta.getPrecio() == null) {
                response.put("success", false);
                response.put("message", "El precio es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            // Establecer valores por defecto
            if (oferta.getFechaExtraccion() == null) {
                oferta.setFechaExtraccion(LocalDateTime.now());
            }

            if (oferta.getFuente() == null || oferta.getFuente().trim().isEmpty()) {
                oferta.setFuente("ADMIN_PANEL");
            }

            oferta.setActiva(true);
            oferta.setFechaCreacion(LocalDateTime.now());

            // Guardar en la base de datos
            Oferta nuevaOferta = ofertasDAO.save(oferta);

            System.out.println("✓ Nueva oferta creada con ID: " + nuevaOferta.getIdOferta());

            response.put("success", true);
            response.put("message", "Promoción creada exitosamente");
            response.put("data", nuevaOferta);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("ERROR al crear oferta:");
            e.printStackTrace();

            response.put("success", false);
            response.put("message", "Error al crear la promoción: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar una oferta existente
     * PUT /api/ofertas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateOferta(
            @PathVariable Long id,
            @RequestBody Oferta ofertaActualizada) {

        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Oferta> ofertaExistente = ofertasDAO.findById(id);

            if (ofertaExistente.isEmpty()) {
                response.put("success", false);
                response.put("message", "Oferta no encontrada");
                return ResponseEntity.notFound().build();
            }

            Oferta oferta = ofertaExistente.get();

            // Actualizar campos
            if (ofertaActualizada.getNombre() != null) {
                oferta.setNombre(ofertaActualizada.getNombre());
            }
            if (ofertaActualizada.getDescripcion() != null) {
                oferta.setDescripcion(ofertaActualizada.getDescripcion());
            }
            if (ofertaActualizada.getPrecio() != null) {
                oferta.setPrecio(ofertaActualizada.getPrecio());
            }
            if (ofertaActualizada.getEtiquetas() != null) {
                oferta.setEtiquetas(ofertaActualizada.getEtiquetas());
            }
            if (ofertaActualizada.getUrl() != null) {
                oferta.setUrl(ofertaActualizada.getUrl());
            }

            oferta.setActiva(ofertaActualizada.isActiva());

            Oferta ofertaGuardada = ofertasDAO.save(oferta);

            response.put("success", true);
            response.put("message", "Promoción actualizada exitosamente");
            response.put("data", ofertaGuardada);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al actualizar la promoción: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Eliminar (desactivar) una oferta
     * DELETE /api/ofertas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOferta(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Oferta> ofertaExistente = ofertasDAO.findById(id);

            if (ofertaExistente.isEmpty()) {
                response.put("success", false);
                response.put("message", "Oferta no encontrada");
                return ResponseEntity.notFound().build();
            }

            Oferta oferta = ofertaExistente.get();

            // Borrado lógico
            oferta.setActiva(false);
            ofertasDAO.save(oferta);

            response.put("success", true);
            response.put("message", "Promoción desactivada exitosamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al eliminar la promoción: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

