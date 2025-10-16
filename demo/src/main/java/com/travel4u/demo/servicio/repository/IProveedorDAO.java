// En: src/main/java/com/travel4u/demo/servicio/repository/IProveedorDAO.java
package com.travel4u.demo.servicio.repository;

import com.travel4u.demo.servicio.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProveedorDAO extends JpaRepository<Proveedor, Integer> {}