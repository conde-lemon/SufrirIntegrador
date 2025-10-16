package com.travel4u.demo.servicio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "proveedor") // Es buena práctica siempre definir el nombre de la tabla
public class Proveedor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private int idProveedor;

    private String nombre;

    @Column(name = "tipo_proveedor")
    private String tipoProveedor;

    private String contacto;
    private String email;
    private String telefono;
}