package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Paquete;
import com.travel4u.demo.reserva.repository.IPaqueteDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar Paquetes
 * Base URL: /api/paquetes
 */
@RestController
@RequestMapping("/api/paquetes")
@CrossOrigin(origins = "*")
public class PaqueteController {

    private final IPaqueteDAO paqueteDAO;

    public PaqueteController(IPaqueteDAO paqueteDAO) {
        this.paqueteDAO = paqueteDAO;
    }

    /**
     * Obtener todos los paquetes
     * GET /api/paquetes
     */
    @GetMapping
    public ResponseEntity<List<Paquete>> getAllPaquetes() {
        try {
            List<Paquete> paquetes = paqueteDAO.findAll();
            return ResponseEntity.ok(paquetes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener un paquete por ID
     * GET /api/paquetes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Paquete> getPaqueteById(@PathVariable Integer id) {
        try {
            Optional<Paquete> paquete = paqueteDAO.findById(id);
            return paquete.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crear nuevo paquete
     * POST /api/paquetes
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPaquete(@RequestBody Paquete paquete) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validaciones básicas
            if (paquete.getNombre() == null || paquete.getNombre().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El nombre del paquete es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            if (paquete.getPrecioTotal() == null) {
                response.put("success", false);
                response.put("message", "El precio total es requerido");
                return ResponseEntity.badRequest().body(response);
            }

            if (paquete.getFechaPaquete() == null) {
                response.put("success", false);
                response.put("message", "La fecha del paquete es requerida");
                return ResponseEntity.badRequest().body(response);
            }


            // Guardar en la base de datos
            Paquete nuevoPaquete = paqueteDAO.save(paquete);

            System.out.println("✓ Nuevo paquete creado con ID: " + nuevoPaquete.getIdPaquete());

            response.put("success", true);
            response.put("message", "Paquete creado exitosamente");
            response.put("data", nuevoPaquete);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("ERROR al crear paquete:");
            e.printStackTrace();

            response.put("success", false);
            response.put("message", "Error al crear el paquete: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar un paquete existente
     * PUT /api/paquetes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePaquete(
            @PathVariable Integer id,
            @RequestBody Paquete paqueteActualizado) {

        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Paquete> paqueteExistente = paqueteDAO.findById(id);

            if (paqueteExistente.isEmpty()) {
                response.put("success", false);
                response.put("message", "Paquete no encontrado");
                return ResponseEntity.notFound().build();
            }

            Paquete paquete = paqueteExistente.get();

            // Actualizar campos
            if (paqueteActualizado.getNombre() != null) {
                paquete.setNombre(paqueteActualizado.getNombre());
            }
            if (paqueteActualizado.getDescripcion() != null) {
                paquete.setDescripcion(paqueteActualizado.getDescripcion());
            }
            if (paqueteActualizado.getPrecioTotal() != null) {
                paquete.setPrecioTotal(paqueteActualizado.getPrecioTotal());
            }
            if (paqueteActualizado.getFechaPaquete() != null) {
                paquete.setFechaPaquete(paqueteActualizado.getFechaPaquete());
            }


            Paquete paqueteGuardado = paqueteDAO.save(paquete);

            response.put("success", true);
            response.put("message", "Paquete actualizado exitosamente");
            response.put("data", paqueteGuardado);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al actualizar el paquete: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Eliminar un paquete
     * DELETE /api/paquetes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePaquete(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Paquete> paqueteExistente = paqueteDAO.findById(id);

            if (paqueteExistente.isEmpty()) {
                response.put("success", false);
                response.put("message", "Paquete no encontrado");
                return ResponseEntity.notFound().build();
            }

            // Eliminar el paquete
            paqueteDAO.deleteById(id);

            response.put("success", true);
            response.put("message", "Paquete eliminado exitosamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al eliminar el paquete: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

