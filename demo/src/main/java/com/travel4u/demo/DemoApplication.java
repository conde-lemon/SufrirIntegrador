package com.travel4u.demo;

import com.travel4u.demo.reserva.model.Detalle_Reserva;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IDetalleReservaDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.servicio.model.Proveedor;
import com.travel4u.demo.servicio.model.Servicio;
import com.travel4u.demo.servicio.repository.IProveedorDAO;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@Transactional
	public CommandLineRunner dataInitializer(
			IUsuarioDAO usuarioDAO,
			IProveedorDAO proveedorDAO,
			IServicioDAO servicioDAO,
			IReservaDAO reservaDAO,
			IDetalleReservaDAO detalleReservaDAO
	) {
		return args -> {
			System.out.println("--- [MODO DEBUG] Inicializando datos de prueba ---");

			// 1. Crear Usuarios
			Usuario admin = null;
			if (usuarioDAO.findByEmail("admin@travel4u.com").isEmpty()) {
				admin = new Usuario();
				admin.setNombres("Administrador");
				admin.setApellidos("del Sistema");
				admin.setEmail("admin@travel4u.com");
				admin.setPassword("1234"); // Contraseña en texto plano
				admin.setRol("ADMIN");
				admin.setActivo(true);
				admin.setFechaRegistro(LocalDateTime.now());
				usuarioDAO.save(admin);
				System.out.println("✓ Usuario 'admin' creado.");
			}

			Usuario cliente = null;
			if (usuarioDAO.findByEmail("cliente@travel4u.com").isEmpty()) {
				cliente = new Usuario();
				cliente.setNombres("Carlos");
				cliente.setApellidos("Cliente");
				cliente.setEmail("cliente@travel4u.com");
				cliente.setPassword("1234"); // Contraseña en texto plano
				cliente.setRol("CLIENTE");
				cliente.setActivo(true);
				cliente.setFechaRegistro(LocalDateTime.now());
				cliente = usuarioDAO.save(cliente); // Guardar y obtener la entidad con ID
				System.out.println("✓ Usuario 'cliente' creado.");
			} else {
				cliente = usuarioDAO.findByEmail("cliente@travel4u.com").get();
			}

			// 2. Crear Proveedores y Servicios (solo si no existen)
			Servicio vueloMadrid = null;
			if (servicioDAO.count() == 0) {
				Proveedor latam = proveedorDAO.save(new Proveedor(null, "Latam Airlines", "AEROLINEA", "contacto@latam.com", "contacto@latam.com", "123456789", true));
				Proveedor iberia = proveedorDAO.save(new Proveedor(null, "Iberia", "AEROLINEA", "contacto@iberia.com", "contacto@iberia.com", "987654321", true));

				vueloMadrid = new Servicio(null, "VUELO", "Vuelo a Madrid", "Peru", "Espana", "vacaciones,familiar", new BigDecimal("1250.50"), 15, "Explora la capital de España.", iberia, true, null);
				Servicio vueloCusco = new Servicio(null, "VUELO", "Vuelo Económico a Cusco", "Peru", "Peru", "aventura", new BigDecimal("150.00"), 20, "Descubre la magia de Cusco.", latam, true, null);
				servicioDAO.save(vueloMadrid);
				servicioDAO.save(vueloCusco);
				System.out.println("✓ Proveedores y Servicios creados.");
			} else {
				vueloMadrid = servicioDAO.findByNombre("Vuelo a Madrid").orElse(null);
			}


			// 3. Crear Reservas (solo si no existen y si el cliente y el vuelo existen)
			if (reservaDAO.count() == 0 && cliente != null && vueloMadrid != null) {
				Reserva reservaMadrid = new Reserva();
				reservaMadrid.setUsuario(cliente);
				reservaMadrid.setEstado("Confirmada");
				reservaMadrid.setTotal(new BigDecimal("1250.50"));
				reservaMadrid.setFechaInicio(LocalDateTime.of(2025, 11, 15, 8, 0));
				reservaMadrid.setMoneda("PEN");
				reservaMadrid.setObservaciones("Vuelo directo a Madrid.");
				reservaDAO.save(reservaMadrid);

				Detalle_Reserva detalleMadrid = new Detalle_Reserva();
				detalleMadrid.setReserva(reservaMadrid);
				detalleMadrid.setServicio(vueloMadrid);
				detalleMadrid.setCantidad(1);
				detalleMadrid.setPrecioUnitario(vueloMadrid.getPrecioBase());
				detalleMadrid.setSubtotal(vueloMadrid.getPrecioBase());
				detalleReservaDAO.save(detalleMadrid);

				System.out.println("✓ Reservas de prueba creadas.");
			}

			System.out.println("--- [MODO DEBUG] Inicialización finalizada ---");
		};
	}
}