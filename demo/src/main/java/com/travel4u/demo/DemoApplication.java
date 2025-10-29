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

	// El logger que usaremos para registrar eventos.
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		// Log de INFO que se mostrará al iniciar.
		log.info("Iniciando la aplicación Travel4U...");
		SpringApplication.run(DemoApplication.class, args);
		log.info("Aplicación Travel4U iniciada y lista para recibir peticiones.");
	}

	/**
	 * Bean que se ejecuta al iniciar la aplicación para verificar y/o crear
	 * una cuenta de administrador, ahora con logs más detallados.
	 */
	@Bean
	@Transactional
	public CommandLineRunner adminAccountVerifier(IUsuarioDAO usuarioDAO, PasswordEncoder passwordEncoder) {
		return args -> {
			log.info("--- [STARTUP] Verificando cuenta de administrador ---");

			String adminEmail = "admin@travel4u.com";
			String adminPassword = "1234";

			// Log de DEBUG: Solo se mostrará porque en logback-spring.xml configuramos
			// el paquete 'com.travel4u.demo' con nivel DEBUG.
			log.debug("Buscando usuario administrador con email: {}", adminEmail);

			Optional<Usuario> adminOptional = usuarioDAO.findByEmail(adminEmail);

			if (adminOptional.isEmpty()) {
				// Log de WARN: Se mostrará en amarillo (o el color configurado).
				log.warn("⚠️ Usuario '{}' no encontrado. Creando uno nuevo...", adminEmail);

				Usuario admin = new Usuario();
				admin.setNombres("Administrador");
				admin.setApellidos("del Sistema");
				admin.setEmail(adminEmail);
				admin.setPassword(passwordEncoder.encode(adminPassword));
				admin.setRol("ADMIN");
				admin.setActivo(true);
				admin.setFechaRegistro(LocalDateTime.now());
				usuarioDAO.save(admin);

				log.info("✓ Usuario '{}' creado exitosamente.", adminEmail);

			} else {
				log.info("✓ Usuario '{}' ya existe. Verificando estado...", adminEmail);
				Usuario existingAdmin = adminOptional.get();

				log.debug("Datos del admin encontrado: ID={}, Rol={}, Activo={}",
						existingAdmin.getIdUsuario(),
						existingAdmin.getRol(),
						existingAdmin.isActivo());

				if (passwordEncoder.matches(adminPassword, existingAdmin.getPassword())) {
					log.info("  ✓ ¡ÉXITO! La contraseña es correcta.");
				} else {
					// Log de ERROR: Se mostrará en rojo (o el color configurado).
					log.error("  ❌ ¡FALLO! La contraseña NO coincide. La guardada es '{}'.", existingAdmin.getPassword());
					log.error("  -> Solución: Borra el usuario de la BD y reinicia la app para que se cree correctamente.");
				}

				if (existingAdmin.isActivo()) {
					log.info("  ✓ El usuario está ACTIVO.");
				} else {
					log.warn("  ⚠️ El usuario está INACTIVO. El inicio de sesión fallará. Considere activarlo en la BD.");
				}
			}
			log.info("--- [STARTUP] Verificación de cuenta de administrador finalizada ---");
		};
	}
}S