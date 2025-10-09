package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="detalle_reserva")

public class Detalle_Reserva {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id_detalle_reserva;

    private int cantidad;
    private float precio_unitario;

    @OneToOne
    @JoinColumn(name="id_reserva", referencedColumnName = "id_reserva")
    private Reserva reserva;

}
