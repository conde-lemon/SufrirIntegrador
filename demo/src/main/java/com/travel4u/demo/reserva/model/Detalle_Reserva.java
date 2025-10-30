package com.travel4u.demo.reserva.model;

import com.travel4u.demo.servicio.model.Servicio;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="detalle_reserva")
public class Detalle_Reserva {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_detalle_reserva")
    private Integer idDetalleReserva; // CAMBIO: a camelCase

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario; // CAMBIO: de float a BigDecimal

    // NUEVO: Campo que estaba en el SQL
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    // CORRECCIÓN: La relación es ManyToOne, no OneToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_reserva", nullable = false)
    @ToString.Exclude
    private Reserva reserva;

    // NUEVO: Relación con Servicio que faltaba
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_servicio", nullable = false)
    @ToString.Exclude
    private Servicio servicio;

    // NUEVO: Campo de auditoría
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}