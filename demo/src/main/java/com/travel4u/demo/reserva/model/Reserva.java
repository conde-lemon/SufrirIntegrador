package com.travel4u.demo.reserva.model;

import com.travel4u.demo.usuario.model.Usuario;
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
@Table(name="reserva")

public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_reserva;
    private LocalDateTime fecha_reserva;
    private String estado;
    private float total;

    @OneToMany(mappedBy = "reserva")
    private Set<Paquete_Reserva> paquete_reservas;

    @ManyToOne
    @JoinColumn(name="id_usuario",referencedColumnName = "id_usuario")
    private Usuario usuario;
}
