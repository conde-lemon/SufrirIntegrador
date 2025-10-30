package com.travel4u.demo.reserva.model;

import com.travel4u.demo.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "idReserva")
@AllArgsConstructor
@NoArgsConstructor
@Table(name="reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva; // CAMBIO: a camelCase y tipo Wrapper

    @Column(nullable = false)
    private String estado;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal total; // CAMBIO: de float a BigDecimal

    // NUEVO: Campos que coinciden con la BD
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    private String moneda;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "reserva")
    private Set<Paquete_Reserva> paqueteReservas; // CAMBIO: a camelCase

    // NUEVO: Campos de auditoría
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // En la clase Reserva.java

    @OneToMany(mappedBy = "reserva", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Detalle_Reserva> detalleReservas;


    // El campo 'fecha_reserva' del SQL es manejado por 'createdAt'
}