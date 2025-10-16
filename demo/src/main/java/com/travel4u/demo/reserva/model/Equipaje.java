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
    @Column(name = "id_equipaje")
    private int idEquipaje;

    private String tipo;

    @Column(name = "peso_max")
    private float pesoMax;

    private float precio;

    @Column(name = "dimension_largo")
    private float dimensionLargo;

    @Column(name = "dimension_ancho")
    private float dimensionAncho;

    @Column(name = "dimension_alto")
    private float dimensionAlto;

    @OneToMany(mappedBy = "equipaje")
    private Set<Reserva_Equipaje> reserva;
}