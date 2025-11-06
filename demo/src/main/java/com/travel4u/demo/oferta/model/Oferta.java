package com.travel4u.demo.oferta.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "oferta")
@Data
public class Oferta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferta")
    private Long idOferta;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "precio", nullable = false)
    private BigDecimal precio;
    
    @Column(name = "descuento")
    private BigDecimal descuento;
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Column(name = "etiquetas")
    private String etiquetas;
    
    @Column(name = "url")
    private String url;
    
    @Column(name = "fuente")
    private String fuente;
    
    @Column(name = "fecha_extraccion")
    private LocalDateTime fechaExtraccion;
    
    @Column(name = "activa")
    private Boolean activa = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}