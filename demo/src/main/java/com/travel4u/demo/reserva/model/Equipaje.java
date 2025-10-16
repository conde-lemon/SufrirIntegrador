// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/reserva/model/Equipaje.java
package com.travel4u.demo.reserva.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal; // CAMBIO: Usar BigDecimal para dinero
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="equipaje")
public class Equipaje {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_equipaje")
    private Integer idEquipaje;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "peso_max", precision = 8, scale = 2, nullable = false)
    private BigDecimal pesoMax; // CAMBIO: a BigDecimal

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio; // CAMBIO: a BigDecimal

    @Column(name = "dimension_largo", precision = 8, scale = 2, nullable = false)
    private BigDecimal dimensionLargo; // CAMBIO: a BigDecimal

    @Column(name = "dimension_ancho", precision = 8, scale = 2, nullable = false)
    private BigDecimal dimensionAncho; // CAMBIO: a BigDecimal

    @Column(name = "dimension_alto", precision = 8, scale = 2, nullable = false)
    private BigDecimal dimensionAlto; // CAMBIO: a BigDecimal

    // NUEVO: Campo de descripción que estaba en el SQL.
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // NUEVO: Campo para borrado lógico.
    private boolean activo = true;

    @OneToMany(mappedBy = "equipaje")
    private Set<Reserva_Equipaje> reserva;
}