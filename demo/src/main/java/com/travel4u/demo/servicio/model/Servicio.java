// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/servicio/model/Servicio.java
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
    @Column(name = "id_servicio")
    private Long idServicio; // CORREGIDO: de int a Long para coincidir con IServicioDAO

    @Column(name = "tipo_servicio")
    private String tipoServicio; // CORREGIDO: a camelCase

    private String nombre;

    @Column(name = "precio_base")
    private float precioBase; // CORREGIDO: a camelCase

    private int disponibilidad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="id_reserva", referencedColumnName = "id_reserva")
    private Detalle_Reserva detalleReservaServicio; // CORREGIDO: a camelCase
}