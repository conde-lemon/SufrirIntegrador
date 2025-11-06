package com.travel4u.demo.oferta.repository;

import com.travel4u.demo.oferta.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOfertaDAO extends JpaRepository<Oferta, Long> {
    
    List<Oferta> findByActivaTrueOrderByFechaCreacionDesc();
    
    List<Oferta> findByEtiquetasContainingIgnoreCase(String etiqueta);
}