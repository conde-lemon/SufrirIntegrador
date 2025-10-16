// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/usuario/model/Usuario.java
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
@Table(name="usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nombres;
    private String apellidos;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String telefono;

    @Column(nullable = false)
    private String rol;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    // NUEVO: Campo para borrado lógico, coincide con la BD.
    private boolean activo = true;

    @OneToMany(mappedBy = "usuario")
    private Set<Reserva> reservas;
}