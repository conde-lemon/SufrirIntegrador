package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="paquete_reserva")

public class Paquete_Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_paquete_reserva;

    @ManyToOne
    @JoinColumn(name="id_reserva",referencedColumnName = "id_reserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name="id_paquete", referencedColumnName = "id_paquete")
    private Paquete paquete;
}
