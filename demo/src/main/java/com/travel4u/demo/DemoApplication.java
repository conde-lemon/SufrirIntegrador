// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/DemoApplication.java
package com.travel4u.demo;

import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * Bean que se ejecuta al iniciar la aplicación para verificar datos.
	 * 1. Crea un usuario 'admin' si no existe.
	 * 2. Lista todos los usuarios en la consola para depuración.
	 */
	@Bean
	@Transactional
	public CommandLineRunner dataLoader(IUsuarioDAO usuarioDAO, PasswordEncoder passwordEncoder) {
		return args -> {
			System.out.println("--- [MODO DEBUG] Verificando usuarios en la base de datos ---");

			// 1. Crear usuario admin si no existe
			String adminEmail = "admin@travel4u.com";
			if (usuarioDAO.findByEmail(adminEmail).isEmpty()) {
				Usuario admin = new Usuario();
				admin.setNombres("Administrador");
				admin.setApellidos("del Sistema");
				admin.setEmail(adminEmail);
				admin.setPassword(passwordEncoder.encode("1234")); // Contraseña '1234' encriptada
				admin.setRol("ADMIN"); // Rol de administrador
				admin.setActivo(true);
				admin.setFechaRegistro(LocalDateTime.now());
				usuarioDAO.save(admin);
				System.out.println(" Usuario 'admin' creado con contraseña '1234'.");
			} else {
				System.out.println(" Usuario 'admin' ya existe.");
			}

			// 2. Listar todos los usuarios para verificar
			List<Usuario> usuarios = usuarioDAO.findAll();
			if (usuarios.isEmpty()) {
				System.out.println(" No se encontraron usuarios en la base de datos.");
			} else {
				System.out.println(" Usuarios encontrados (" + usuarios.size() + "):");
				for (Usuario u : usuarios) {
					System.out.println("  -> ID: " + u.getIdUsuario() +
							", Email: " + u.getEmail() +
							", Rol: " + u.getRol() +
							", Contraseña (Encriptada): " + u.getPassword());
				}
			}
			System.out.println("--- [MODO DEBUG] Verificación finalizada ---");
		};
	}
}