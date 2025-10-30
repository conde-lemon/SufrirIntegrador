package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable; // Importar
import java.util.List; // Asegúrate de importar List

@Repository
public interface IReservaDAO extends JpaRepository<Reserva, Integer> {

// En IReservaDAO.java


    List<Reserva> findByUsuarioEmailOrderByCreatedAtDesc(String email, Pageable pageable);

    List<Reserva> findByUsuarioOrderByCreatedAtDesc(Usuario usuario);
}