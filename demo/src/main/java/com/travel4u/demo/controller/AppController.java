// C:/Users/LENOVO/Documents/utp/ciclo7/integrador/demo (1)/demo/src/main/java/com/travel4u/demo/controllers/AppController.java
package com.travel4u.demo.controller;

import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    private IReservaDAO reservaDAO;

    /**
     * Muestra la página de inicio y le pasa la lista de todas las reservas.
     * Este es el método correcto para la ruta raíz.
     */
    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Reserva> listaReservas = reservaDAO.findAll();
        model.addAttribute("reservas", listaReservas); // Pasa la lista a la plantilla index.html
        return "index"; // Devuelve el nombre del archivo de plantilla (index.html)
    }

    /**
     * Muestra la página de login.
     */
    @GetMapping("/login")
    public String viewLoginPage() {
        return "login"; // Devuelve el nombre del archivo de plantilla (login.html)
    }

    // --- MÉTODOS MOVIDOS DESDE NAVIGATIONCONTROLLER ---

    @GetMapping("/vuelos")
    public String showVuelosPage() {
        return "vuelos";
    }

    @GetMapping("/cruceros")
    public String showCrucerosPage() {
        return "cruceros";
    }

    @GetMapping("/bus")
    public String showBusPage() {
        return "bus";
    }

    @GetMapping("/hospedaje")
    public String showHospedajePage() {
        return "hospedaje";
    }

    @GetMapping("/confirmacion-reserva")
    public String showConfirmacionReservaPage() {
        return "confirmacion-reserva"; // Asume que tienes un confirmacion-reserva.html
    }

    @GetMapping("/terminos-y-condiciones")
    public String showTerminosPage() {
        return "terminos_y_condiciones"; // Asume que tienes un terminos_y_condiciones.html
    }
}