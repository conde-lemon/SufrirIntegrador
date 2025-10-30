// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/reserva/model/Paquete.java
package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.*; // <-- Importa las nuevas anotaciones
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="paquete")
// --- REEMPLAZA @Data POR ESTO ---
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "idPaquete")
// ---------------------------------
@AllArgsConstructor
@NoArgsConstructor
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
    private BigDecimal precioTotal;

    @OneToMany(mappedBy = "paquete")
    @ToString.Exclude // <-- ¡MUY IMPORTANTE para romper el ciclo!
    private Set<Paquete_Reserva> paqueteReservas;

    @Column(name = "fecha_paquete")
    private LocalDateTime fechaPaquete;

    private boolean activo = true;
}