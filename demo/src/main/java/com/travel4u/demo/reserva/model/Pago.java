// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/reserva/model/Pago.java
package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal; // CAMBIO: Usar BigDecimal para dinero
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
    private Integer idPago;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal monto; // CAMBIO: a BigDecimal

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @Column(name = "estado_pago", nullable = false)
    private String estadoPago;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    // NUEVO: Campo que estaba en el SQL
    @Column(name = "referencia_pago")
    private String referenciaPago;

    @OneToOne
    @JoinColumn(name="id_reserva", referencedColumnName = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    // NUEVO: Campos de auditoría
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}