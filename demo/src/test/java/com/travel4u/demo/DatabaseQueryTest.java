package com.travel4u.demo;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DatabaseQueryTest {

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private IReservaDAO reservaDAO;

    @Test
    void testConsultarDatosExistentes() {
        System.out.println("=== PRUEBA DE CONSULTAS A LA BASE DE DATOS ===");
        
        // Consultar todos los usuarios
        List<Usuario> usuarios = usuarioDAO.findAll();
        System.out.println("[DEBUG] Total usuarios en BD: " + usuarios.size());
        
        for (Usuario u : usuarios) {
            System.out.println("[DEBUG] Usuario: ID=" + u.getIdUsuario() + 
                             ", Email=" + u.getEmail() + 
                             ", Nombre=" + u.getNombres() + " " + u.getApellidos());
        }
        
        // Consultar todas las reservas
        List<Reserva> reservas = reservaDAO.findAll();
        System.out.println("[DEBUG] Total reservas en BD: " + reservas.size());
        
        for (Reserva r : reservas) {
            System.out.println("[DEBUG] Reserva: ID=" + r.getIdReserva() + 
                             ", Estado=" + r.getEstado() + 
                             ", Total=" + r.getTotal() + 
                             ", Usuario=" + (r.getUsuario() != null ? r.getUsuario().getEmail() : "null"));
        }
        
        // Probar consulta específica por usuario
        if (!usuarios.isEmpty()) {
            Usuario primerUsuario = usuarios.get(0);
            List<Reserva> reservasUsuario = reservaDAO.findByUsuarioOrderByCreatedAtDesc(primerUsuario);
            System.out.println("[DEBUG] Reservas del usuario " + primerUsuario.getEmail() + ": " + reservasUsuario.size());
        }
        
        System.out.println("=== FIN DE PRUEBAS ===");
    }
}