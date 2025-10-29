package com.travel4u.demo.servicio.model;

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
@Table(name="servicio")
public class Servicio {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;

    @Column(name = "tipo_servicio", nullable = false)
    private String tipoServicio; // "VUELO", "CRUCERO", etc.

    @Column(nullable = false)
    private String nombre;

    // NUEVOS CAMPOS PARA VUELOS
    @Column(name = "origen")
    private String origen; // Ej: "Peru"

    @Column(name = "destino")
    private String destino; // Ej: "Espana"

    @Column(name = "tags")
    private String tags; // Ej: "vacaciones,top ventas,familiar"

    @Column(name = "precio_base", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioBase;

    @Column(nullable = false)
    private int disponibilidad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_proveedor")
    private Proveedor proveedor;

    private boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}