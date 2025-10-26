package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.usuario.model.Usuario; // Asegúrate de importar Usuario
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Asegúrate de importar List

@Repository
public interface IReservaDAO extends JpaRepository<Reserva, Integer> {

    // NUEVO MÉTODO: Spring Data JPA creará automáticamente la consulta
    // para encontrar todas las reservas que pertenecen a un usuario específico.
    List<Reserva> findByUsuario(Usuario usuario);

}