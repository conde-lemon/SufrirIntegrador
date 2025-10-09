package com.travel4u.demo.servicio.model;

import com.travel4u.demo.reserva.model.Detalle_Reserva;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="servicio")

public class Servicio {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id_servicio;
    private String tipo_servicio;
    private String nombre;
    private float precio_base;
    private int disponibilidad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="id_reserva", referencedColumnName = "id_reserva")
    private Detalle_Reserva detalle_reserva_servicio;

}
