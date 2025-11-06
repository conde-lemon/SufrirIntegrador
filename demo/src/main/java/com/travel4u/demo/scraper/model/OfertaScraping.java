package com.travel4u.demo.scraper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Usamos Lombok para reducir el código boilerplate (getters, setters, etc.)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfertaScraping {
    private String titulo;
    private String descripcion;
    private String precio;
    private String tipo; // "Vuelo", "Crucero", "Bus", "Hospedaje"
    private String imagenUrl;
    private String urlDestino; // A dónde redirigir al hacer clic
    private String tag; // "Top Ventas", "Familiar", etc.
    private String tagClass; // Clase CSS para el tag (e.g., 'tag-top', 'tag-familiar')
}