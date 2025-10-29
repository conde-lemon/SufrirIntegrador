// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/reserva/repository/IReservaDAO.java
package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservaDAO extends JpaRepository<Reserva, Long> { // <-- CAMBIO: De Integer a Long

    // Este método para encontrar por usuario sigue funcionando igual.
    List<Reserva> findByUsuario(Usuario usuario);

}