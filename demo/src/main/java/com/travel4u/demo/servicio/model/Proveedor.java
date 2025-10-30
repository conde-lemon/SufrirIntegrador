// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/servicio/model/Proveedor.java
package com.travel4u.demo.servicio.model;

import jakarta.persistence.*;
import lombok.*; // <-- Importa las nuevas anotaciones

@Entity
@Table(name = "proveedor")
// --- REEMPLAZA @Data POR ESTO ---
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "idProveedor")
// ---------------------------------
@AllArgsConstructor
@NoArgsConstructor
public class Proveedor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "tipo_proveedor", nullable = false)
    private String tipoProveedor;

    private String contacto;
    private String email;
    private String telefono;

    private boolean activo = true;
}