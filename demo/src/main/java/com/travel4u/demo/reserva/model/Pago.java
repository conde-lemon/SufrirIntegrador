package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="pago")
public class Pago {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private int idPago;

    private float monto;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "estado_pago")
    private String estadoPago;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @OneToOne
    @JoinColumn(name="id_reserva", referencedColumnName = "id_reserva")
    private Reserva reserva;
}