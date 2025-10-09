package com.travel4u.demo.usuario.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="historial")

public class Historial {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id_historial;

    private String termino_busqueda;
    private LocalDateTime fecha;

    @OneToOne
    @JoinColumn(name="id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;
}
