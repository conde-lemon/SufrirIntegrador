// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/reserva/model/Paquete.java
package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal; // CAMBIO: Usar BigDecimal para dinero
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="paquete")
public class Paquete {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_paquete")
    private Integer idPaquete;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioTotal; // CAMBIO: a BigDecimal

    @OneToMany(mappedBy = "paquete")
    private Set<Paquete_Reserva> paqueteReservas;

    @Column(name = "fecha_paquete")
    private LocalDateTime fechaPaquete;
}