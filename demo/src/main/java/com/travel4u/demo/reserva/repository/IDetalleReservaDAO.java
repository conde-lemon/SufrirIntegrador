package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Detalle_Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel4u.demo.reserva.model.Reserva;
import java.util.Optional;

/**
 * Repositorio para la entidad Detalle_Reserva.
 * Proporciona métodos CRUD (Crear, Leer, Actualizar, Borrar) básicos
 * gracias a la herencia de JpaRepository.
 */
@Repository
public interface IDetalleReservaDAO extends JpaRepository<Detalle_Reserva, Integer> {
    Optional<Detalle_Reserva> findByReserva(Reserva reserva);
}