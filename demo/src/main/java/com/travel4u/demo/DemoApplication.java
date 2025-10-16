// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/DemoApplication.java
package com.travel4u.demo;

import com.travel4u.demo.ofertas.model.Oferta;
import com.travel4u.demo.ofertas.repository.IOfertasDAO;
import com.travel4u.demo.reserva.model.Equipaje;
import com.travel4u.demo.reserva.model.Pago;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IEquipajeDAO;
import com.travel4u.demo.reserva.repository.IPagoDAO;
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

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@Transactional // Asegura que todas las operaciones se ejecuten en una sola transacción
	public CommandLineRunner loadData(
			IUsuarioDAO usuarioDAO,
			IReservaDAO reservaDAO,
			IPagoDAO pagoDAO,
			IProveedorDAO proveedorDAO,
			IServicioDAO servicioDAO,
			IEquipajeDAO equipajeDAO,
			IOfertasDAO ofertasDAO) {
		return args -> {
			System.out.println("--- Cargando datos de prueba ---");

			// 1. Crear entidades independientes
			Usuario usuario1 = new Usuario(null, "Ana", "García", "ana.garcia@email.com", "pass123", "987654321", "CLIENTE", LocalDateTime.now(), null);
			Usuario usuario2 = new Usuario(null, "Carlos", "Ruiz", "carlos.ruiz@email.com", "pass456", "912345678", "CLIENTE", LocalDateTime.now(), null);
			usuarioDAO.saveAll(List.of(usuario1, usuario2));
			System.out.println("✅ 2 Usuarios guardados.");

			Proveedor proveedor1 = new Proveedor(0, "Latam Airlines", "AEROLINEA", "Juan Pérez", "contacto@latam.com", "555-1111");
			proveedorDAO.save(proveedor1);
			System.out.println("✅ 1 Proveedor guardado.");

			Equipaje equipaje1 = new Equipaje(0, "De mano", 10.0f, 50.0f, 40f, 30f, 20f, null);
			equipajeDAO.save(equipaje1);
			System.out.println("✅ 1 Equipaje guardado.");

			Oferta oferta1 = new Oferta(null, "Vuelo a Cusco", "Oferta imperdible para volar a Cusco.", "vuelo,cusco,oferta", 250, "http://example.com/oferta/1", "Despegar", LocalDateTime.now());
			ofertasDAO.save(oferta1);
			System.out.println("✅ 1 Oferta guardada.");

			Servicio servicio1 = new Servicio(null, "ASIENTO_VIP", "Asiento con más espacio", 100.0f, 20, "Disfruta de un viaje más cómodo.", null);
			servicioDAO.save(servicio1);
			System.out.println("✅ 1 Servicio guardado.");

			// 2. Crear entidades dependientes
			Reserva reserva1 = new Reserva(0, LocalDateTime.now(), "CONFIRMADA", 550.0f, null, usuario1);
			reservaDAO.save(reserva1); // Guardar la reserva primero para que obtenga un ID

			Pago pago1 = new Pago(0, 550.0f, "TARJETA_CREDITO", "APROBADO", LocalDateTime.now(), reserva1);
			pagoDAO.save(pago1);
			System.out.println("✅ 1 Reserva y 1 Pago guardados para el usuario: " + usuario1.getNombres());

			System.out.println("--- Carga de datos finalizada ---");
		};
	}
}