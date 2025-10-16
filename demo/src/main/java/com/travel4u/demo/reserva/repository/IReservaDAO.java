// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/reserva/repository/IReservaDAO.java
package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Reserva; // Tu entidad 'Pago' ya la referencia, así que debe existir.
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservaDAO extends JpaRepository<Reserva, Integer> {
    // El segundo parámetro 'Integer' debe coincidir con el tipo de la clave primaria de tu entidad 'Reserva'.
}