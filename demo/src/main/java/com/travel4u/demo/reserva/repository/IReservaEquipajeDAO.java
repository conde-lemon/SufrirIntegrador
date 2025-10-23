package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Reserva_Equipaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservaEquipajeDAO extends JpaRepository<Reserva_Equipaje, Integer> {
}