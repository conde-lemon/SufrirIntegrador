package com.travel4u.demo.servicio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="servicio_proveedor")

public class Servicio_Proveedor {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id_servicio_proveedor;

    @OneToOne
    @JoinColumn(name="id_servicio",referencedColumnName="id_servicio")
    private Servicio servicio;

    @OneToOne
    @JoinColumn(name="id_proveedor",referencedColumnName="id_proveedor")
    private Proveedor proveedor;

}
