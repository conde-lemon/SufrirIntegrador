package com.travel4u.demo;

import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

	// Es una buena práctica usar un logger en lugar de System.out.println
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * Bean que se ejecuta al iniciar la aplicación para verificar y/o crear
	 * una cuenta de administrador.
	 * 1. Busca el usuario 'admin@travel4u.com'.
	 * 2. Si no existe, lo crea con la contraseña '1234' (encriptada).
	 * 3. Si existe, verifica que la contraseña '1234' coincida con el hash guardado.
	 *    También verifica si el usuario está activo.
	 */
	@Bean
	@Transactional
	public CommandLineRunner adminAccountVerifier(IUsuarioDAO usuarioDAO, PasswordEncoder passwordEncoder) {
		return args -> {
			log.info("--- [STARTUP] Verificando cuenta de administrador ---");

			String adminEmail = "admin@travel4u.com";
			String adminPassword = "1234"; // Contraseña en texto plano para la verificación/creación

			Optional<Usuario> adminOptional = usuarioDAO.findByEmail(adminEmail);

			if (adminOptional.isEmpty()) {
				log.warn("⚠️ Usuario '{}' no encontrado. Creando uno nuevo...", adminEmail);

				Usuario admin = new Usuario();
				admin.setNombres("Administrador");
				admin.setApellidos("del Sistema");
				admin.setEmail(adminEmail);

				// ¡IMPORTANTE! Encriptar la contraseña antes de guardarla en la base de datos.
				admin.setPassword(passwordEncoder.encode(adminPassword));

				admin.setRol("ADMIN"); // Asignar el rol de administrador
				admin.setActivo(true); // Asegurarse de que el usuario esté activo
				admin.setFechaRegistro(LocalDateTime.now());
				usuarioDAO.save(admin);
				log.info("✓ Usuario '{}' creado exitosamente con contraseña: '{}'", adminEmail, adminPassword);

			} else {
				log.info("✓ Usuario '{}' ya existe. Verificando contraseña...", adminEmail);
				Usuario existingAdmin = adminOptional.get();

				// Usamos el PasswordEncoder (BCryptPasswordEncoder) para verificar
				// si la contraseña en texto plano coincide con el hash guardado.
				if (passwordEncoder.matches(adminPassword, existingAdmin.getPassword())) {
					log.info("  ✓ ¡ÉXITO! La contraseña '{}' es correcta para el usuario '{}'.", adminPassword, adminEmail);
				} else {
					log.error("  ❌ ¡FALLO! La contraseña NO coincide. La contraseña esperada era '{}' pero la guardada es '{}'.", adminPassword, existingAdmin.getPassword());
					log.error("  -> Solución: Borra el usuario de la BD y reinicia la app para que se cree correctamente con la contraseña encriptada.");
				}

				// Verificamos también si el usuario está activo, ya que es crucial para el login.
				if (existingAdmin.isActivo()) {
					log.info("  ✓ El usuario está ACTIVO.");
				} else {
					log.warn("  ⚠️ El usuario está INACTIVO. El inicio de sesión fallará por esta razón. Considere activarlo en la BD.");
				}
			}
			log.info("--- [STARTUP] Verificación de cuenta de administrador finalizada ---");
		};
	}
}