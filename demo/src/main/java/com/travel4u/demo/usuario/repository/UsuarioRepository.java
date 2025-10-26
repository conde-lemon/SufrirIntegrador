package com.travel4u.demo.usuario.repository;

import com.travel4u.demo.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> { // ← Long con L mayúscula

    Optional<Usuario> findByEmail(String email);

    // Búsqueda por nombres, apellidos o email
    @Query("SELECT u FROM Usuario u WHERE " + "(u.nombres LIKE %:criterio% OR u.apellidos LIKE %:criterio% OR u.email LIKE %:criterio%) " + "AND u.activo = true")
    List<Usuario> buscarUsuariosActivos(@Param("criterio") String criterio);

    // Buscar todos incluyendo inactivos para administración
    @Query("SELECT u FROM Usuario u WHERE " + "u.nombres LIKE %:criterio% OR u.apellidos LIKE %:criterio% OR u.email LIKE %:criterio%")
    List<Usuario> buscarTodosUsuarios(@Param("criterio") String criterio);

    boolean existsByEmailAndIdUsuarioNot(String email, Long id);

    List<Usuario> findByActivoTrue();
}

