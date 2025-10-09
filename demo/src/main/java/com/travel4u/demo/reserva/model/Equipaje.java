package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="equipaje")

public class Equipaje {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id_equipaje;

    private String tipo;
    private float peso_max;
    private float precio;
    private float dimension_largo;
    private float dimension_ancho;
    private float dimension_alto;

    @OneToMany(mappedBy = "equipaje")
    private Set<Reserva_Equipaje> reserva;
}
