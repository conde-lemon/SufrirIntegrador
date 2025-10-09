package com.travel4u.demo.servicio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Proveedor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id_proveedor;

    private String nombre;
    private String tipo_proveedor;
    private String contacto;
    private String email;
    private String telefono;
}
