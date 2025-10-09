package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="reserva_equipaje")

public class Reserva_Equipaje {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id_reserva_equipaje;

    private int cantidad;

    @OneToOne
    @JoinColumn(name="id_reserva",referencedColumnName = "id_reserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_equipaje",referencedColumnName = "id_equipaje")
    private Equipaje equipaje;
}
