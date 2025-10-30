package com.travel4u.demo.ofertas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oferta")
public class Oferta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferta")
    private Long idOferta;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio; // CAMBIO: de int a BigDecimal

    @Column(precision = 5, scale = 2)
    private BigDecimal descuento; // Descuento en porcentaje (ej: 15.50 = 15.5%)

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    private String etiquetas;
    private String url;
    private String fuente;

    @Column(name = "fecha_extraccion")
    private LocalDateTime fechaExtraccion;

    // NUEVO: Campo que coincide con la columna 'activa' en la BD
    private boolean activa = true;

    // NUEVO: Campo de auditoría
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
}