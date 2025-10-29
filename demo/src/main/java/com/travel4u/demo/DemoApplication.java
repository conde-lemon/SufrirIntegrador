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

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * Este CommandLineRunner se encarga únicamente de crear el usuario administrador
	 * si no existe, ya que requiere lógica de encriptación de contraseña que no
	 * se puede hacer en un script SQL.
	 */
	@Bean
	@Transactional
	public CommandLineRunner adminUserInitializer(
			IUsuarioDAO usuarioDAO,
			PasswordEncoder passwordEncoder) {
		return args -> {
			System.out.println("--- [MODO DEBUG] Verificando usuario admin ---");

			// Crear usuario admin si no existe
			if (usuarioDAO.findByEmail("admin@travel4u.com").isEmpty()) {
				Usuario admin = new Usuario();
				admin.setNombres("Administrador");
				admin.setApellidos("del Sistema");
				admin.setEmail("admin@travel4u.com");
				admin.setPassword(passwordEncoder.encode("1234"));
				admin.setRol("ADMIN");
				admin.setActivo(true);
				admin.setFechaRegistro(LocalDateTime.now());
				usuarioDAO.save(admin);
				System.out.println("✓ Usuario 'admin' creado con contraseña encriptada.");
			} else {
				System.out.println("✓ Usuario 'admin' ya existe.");
			}
			System.out.println("--- [MODO DEBUG] Verificación finalizada ---");
		};
	}
}