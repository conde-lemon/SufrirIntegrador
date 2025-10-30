package com.travel4u.demo.reserva.repository;

import com.travel4u.demo.reserva.model.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaqueteDAO extends JpaRepository<Paquete, Integer> {
    // Métodos CRUD heredados de JpaRepository
}

