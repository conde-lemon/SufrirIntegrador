// En: src/main/java/com/travel4u/demo/reserva/repository/IPagoDAO.java
package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Pago;
import com.travel4u.demo.reserva.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPagoDAO extends JpaRepository<Pago, Integer> {
    Optional<Pago> findByReserva(Reserva reserva);
}