package com.travel4u.demo.servicio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="servicio_proveedor")
public class Servicio_Proveedor {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_servicio_proveedor")
    private Integer idServicioProveedor; // CAMBIO: a camelCase

    // CORRECCIÓN: La relación es ManyToOne, no OneToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_servicio", nullable = false)
    private Servicio servicio;

    // CORRECCIÓN: La relación es ManyToOne, no OneToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_proveedor", nullable = false)
    private Proveedor proveedor;

    // NUEVO: Campo de auditoría
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}