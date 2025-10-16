package com.travel4u.demo.usuario.model;

import com.travel4u.demo.reserva.model.Reserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
// CAMBIO 1: Renombrar la tabla para evitar conflicto con palabra reservada
@Table(name="usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // CAMBIO 2: Tipo de ID a Long para consistencia con JpaRepository
    // CAMBIO 3: Nomenclatura camelCase para el campo, mapeando a la columna snake_case
    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private String telefono;
    private String rol;

    // CAMBIO 4: Nomenclatura camelCase para el campo, mapeando a la columna snake_case
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "usuario")
    private Set<Reserva> reservas;
}