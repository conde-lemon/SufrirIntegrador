package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="paquete")

public class Paquete {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id_paquete;

    private String nombre;
    private String descripcion;
    private float precio_total;

    @OneToMany(mappedBy = "paquete")
    private Set<Paquete_Reserva> paquete_reservas;

    //variables ocultas tomada automaticamente]
    private LocalDateTime fecha_paquete;
}
