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
    private String tipoServicio;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "precio_base", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioBase; // CAMBIO: de float a BigDecimal

    @Column(nullable = false)
    private int disponibilidad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // CORRECCIÓN: La relación es con Proveedor, no con Detalle_Reserva
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_proveedor")
    private Proveedor proveedor;

    // NUEVO: Campo para borrado lógico
    private boolean activo = true;

    // NUEVO: Campo de auditoría
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}