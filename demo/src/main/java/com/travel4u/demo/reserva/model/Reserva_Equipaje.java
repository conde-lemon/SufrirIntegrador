package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="reserva_equipaje")
public class Reserva_Equipaje {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_reserva_equipaje")
    private Integer idReservaEquipaje; // CAMBIO: a camelCase

    @Column(nullable = false)
    private int cantidad;

    // NUEVO: Campos que estaban en el SQL
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    // CORRECCIÓN: La relación es ManyToOne, no OneToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_reserva", nullable = false)
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipaje", nullable = false)
    private Equipaje equipaje;

    // NUEVO: Campo de auditoría
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}