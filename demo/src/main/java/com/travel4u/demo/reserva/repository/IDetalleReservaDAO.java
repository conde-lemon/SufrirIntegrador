package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Detalle_Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Detalle_Reserva.
 * Proporciona métodos CRUD (Crear, Leer, Actualizar, Borrar) básicos
 * gracias a la herencia de JpaRepository.
 */
@Repository
public interface IDetalleReservaDAO extends JpaRepository<Detalle_Reserva, Integer> {
    // Por ahora, no se necesitan métodos de consulta personalizados.
    // JpaRepository ya nos proporciona métodos como save(), findById(), findAll(), etc.,
    // que son suficientes para la funcionalidad actual.
}