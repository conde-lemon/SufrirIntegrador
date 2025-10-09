package com.travel4u.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    // Cuando alguien vaya a la URL raíz "/", se mostrará index.html
    @GetMapping("/")
    public String showIndexPage() {
        return "index"; // Devuelve el nombre del archivo "index.html" sin la extensión
    }

    // Maneja los enlaces del iframe y la navegación
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

    // Maneja los enlaces de la página de asientos
    @GetMapping("/confirmacion-reserva")
    public String showConfirmacionReservaPage() {
        return "confirmacion-reserva"; // Asume que tienes un confirmacion-reserva.html
    }

    @GetMapping("/terminos-y-condiciones")
    public String showTerminosPage() {
        return "terminos_y_condiciones"; // Asume que tienes un terminos_y_condiciones.html
    }
}