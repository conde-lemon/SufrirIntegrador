package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="pago")

public class Pago {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id_pago;

    private float monto;
    private String metodo_pago;
    private String estado_pago; // puede ser boolean, se realizo el pago o no (verdadero o falso)
    private java.time.LocalDateTime fecha_pago;

    @OneToOne
    @JoinColumn(name="id_reserva",referencedColumnName = "id_reserva")
    private Reserva reserva;
    //id_reserva de la tabla reserva

}
