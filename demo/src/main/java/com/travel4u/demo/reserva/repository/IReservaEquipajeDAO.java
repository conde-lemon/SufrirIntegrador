package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Reserva; // Importa la clase Reserva
import com.travel4u.demo.reserva.model.Reserva_Equipaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importa la clase List

@Repository
public interface IReservaEquipajeDAO extends JpaRepository<Reserva_Equipaje, Integer> {

    // NUEVO MÉTODO: Encuentra todos los equipajes asociados a una reserva.
    List<Reserva_Equipaje> findByReserva(Reserva reserva);
}