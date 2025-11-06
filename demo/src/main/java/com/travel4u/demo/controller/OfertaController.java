package com.travel4u.demo.controller;

import com.travel4u.demo.oferta.model.Oferta;
import com.travel4u.demo.oferta.repository.IOfertaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    @Autowired
    private IOfertaDAO ofertaDAO;

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearOferta(@RequestBody Map<String, Object> datos) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Oferta oferta = new Oferta();
            oferta.setNombre((String) datos.get("nombre"));
            oferta.setDescripcion((String) datos.get("descripcion"));
            oferta.setPrecio(new BigDecimal(datos.get("precio").toString()));
            oferta.setDescuento(new BigDecimal(datos.get("descuento").toString()));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            oferta.setFechaInicio(LocalDateTime.parse((String) datos.get("fechaInicio"), formatter));
            oferta.setFechaFin(LocalDateTime.parse((String) datos.get("fechaFin"), formatter));
            
            oferta.setEtiquetas((String) datos.get("etiquetas"));
            oferta.setUrl((String) datos.get("url"));
            oferta.setFuente((String) datos.get("fuente"));
            oferta.setFechaExtraccion(LocalDateTime.now());
            oferta.setActiva(true);
            oferta.setFechaCreacion(LocalDateTime.now());
            
            Oferta savedOferta = ofertaDAO.save(oferta);
            
            response.put("success", true);
            response.put("message", "Promoción creada exitosamente");
            response.put("data", Map.of(
                "id", savedOferta.getIdOferta(),
                "nombre", savedOferta.getNombre(),
                "precio", savedOferta.getPrecio(),
                "descuento", savedOferta.getDescuento()
            ));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear promoción: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}