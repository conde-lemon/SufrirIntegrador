// En: src/main/java/com/travel4u/demo/reserva/repository/IEquipajeDAO.java
package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Equipaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEquipajeDAO extends JpaRepository<Equipaje, Integer> {}