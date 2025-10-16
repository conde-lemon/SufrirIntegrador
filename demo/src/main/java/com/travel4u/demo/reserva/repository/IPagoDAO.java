// En: src/main/java/com/travel4u/demo/reserva/repository/IPagoDAO.java
package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPagoDAO extends JpaRepository<Pago, Integer> {}