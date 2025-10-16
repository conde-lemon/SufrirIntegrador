// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/usuario/repository/IUsuarioDAO.java
package com.travel4u.demo.usuario.repository;

import com.travel4u.demo.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importar Optional

@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {
    // Spring Data JPA creará automáticamente los métodos CRUD básicos.

    /**
     * MÉTODO AÑADIDO:
     * Spring Data JPA generará automáticamente la consulta para buscar un usuario
     * por su columna 'email'.
     * Usamos Optional para manejar de forma segura el caso en que el usuario no exista.
     */
    Optional<Usuario> findByEmail(String email);
}