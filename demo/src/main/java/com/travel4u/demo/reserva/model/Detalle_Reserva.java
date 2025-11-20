package com.travel4u.demo.reserva.model;

import com.travel4u.demo.servicio.model.Servicio;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    // Campos de datos del pasajero
    private String nombre;
    private String apellido;
    private String documento;
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    private String sexo;
    private String correo;
    private String telefono;
    
    @Column(name = "asiento_seleccionado")
    private String asientoSeleccionado;
    
    // NUEVO: Campos adicionales para vuelos
    @Column(name = "puerta_abordaje")
    private String puertaAbordaje;
    
    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;
    
    @Column(name = "hora_salida")
    private String horaSalida;

    // Relación 1:1 con Reserva
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_reserva", nullable = false)
    @ToString.Exclude
    private Reserva reserva;

    // Relación con Servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_servicio", nullable = false)
    @ToString.Exclude
    private Servicio servicio;

    // NUEVO: Campo de auditoría
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}