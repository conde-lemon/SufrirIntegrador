// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/ofertas/repository/IOfertasDAO.java
package com.travel4u.demo.ofertas.repository;

import com.travel4u.demo.ofertas.model.Oferta; // Asegúrate de que la entidad 'Oferta' exista en este paquete
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOfertasDAO extends JpaRepository<Oferta, Long> {
    // Métodos CRUD listos para usar.
}