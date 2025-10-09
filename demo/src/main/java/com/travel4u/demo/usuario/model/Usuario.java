package com.travel4u.demo.usuario.model;

import com.travel4u.demo.reserva.model.Reserva;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="user")

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_usuario;

    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private String telefono;
    private String rol;

    //Medida interna, para mas datos
    private LocalDateTime fecha_registro;

    @OneToMany(mappedBy = "usuario")
    private Set<Reserva> reservas;
}
