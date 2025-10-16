// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/ofertas/model/Oferta.java
package com.travel4u.demo.ofertas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oferta") // Es buena práctica definir siempre el nombre de la tabla
public class Oferta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferta")
    private Long idOferta; // CORREGIDO: de int a Long para coincidir con IOfertasDAO

    private String nombre;
    private String descripcion;
    private String etiquetas;
    private int precio;
    private String url;
    private String fuente;

    @Column(name = "fecha_extraccion")
    private LocalDateTime fechaExtraccion; // CORREGIDO: a camelCase
}