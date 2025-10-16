// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/servicio/repository/IServicioDAO.java
package com.travel4u.demo.servicio.repository;

import com.travel4u.demo.servicio.model.Servicio; // Asegúrate de que la entidad 'Servicio' exista en este paquete
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IServicioDAO extends JpaRepository<Servicio, Long> {
    // Métodos CRUD listos para usar.
}