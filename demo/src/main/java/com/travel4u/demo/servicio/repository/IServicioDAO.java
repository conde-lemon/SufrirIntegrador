// En: src/main/java/com/travel4u/demo/servicio/repository/IServicioDAO.java
package com.travel4u.demo.servicio.repository;

import com.travel4u.demo.servicio.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IServicioDAO extends JpaRepository<Servicio, Long> {

    List<Servicio> findByTipoServicioAndOrigenAndDestinoAndActivoTrue(String tipoServicio, String origen, String destino);

    // --- MÉTODO NUEVO PARA SUGERENCIAS ---
    // Busca los 5 primeros vuelos activos para usar como sugerencia.
    List<Servicio> findTop5ByTipoServicioAndActivoTrue(String tipoServicio);
}