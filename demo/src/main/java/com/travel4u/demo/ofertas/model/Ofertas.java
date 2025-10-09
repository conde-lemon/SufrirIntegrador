package com.travel4u.demo.ofertas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor


public class Ofertas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_oferta;

    private String nombre;
    private String descripcion;
    private String etiquetas;
    private int precio;
    private String url;
    private String fuente;
    private LocalDateTime fecha_extraccion;
}
