// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/usuario/repository/IUsuarioDAO.java
package com.travel4u.demo.usuario.repository;

import com.travel4u.demo.usuario.model.Usuario; // Asegúrate de que la entidad 'Usuario' exista en este paquete
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {
    // Spring Data JPA creará automáticamente los métodos CRUD básicos.
    // Puedes añadir métodos de consulta personalizados aquí si los necesitas.
}