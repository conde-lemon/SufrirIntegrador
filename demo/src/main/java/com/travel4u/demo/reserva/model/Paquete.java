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
    @Column(name = "id_paquete")
    private int idPaquete;

    private String nombre;
    private String descripcion;

    @Column(name = "precio_total")
    private float precioTotal;

    @OneToMany(mappedBy = "paquete")
    private Set<Paquete_Reserva> paqueteReservas;

    @Column(name = "fecha_paquete")
    private LocalDateTime fechaPaquete;
}