// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/servicio/model/Proveedor.java
package com.travel4u.demo.servicio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "proveedor")
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

    // NUEVO: Campo para borrado lógico.
    private boolean activo = true;
}