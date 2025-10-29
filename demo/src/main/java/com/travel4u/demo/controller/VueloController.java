package com.travel4u.demo.controller;

import com.travel4u.demo.servicio.VueloService; // Importa el nuevo servicio
import com.travel4u.demo.servicio.model.Servicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VueloController {

    private final VueloService vueloService; // Inyecta el servicio en lugar del DAO

    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    @GetMapping("/vuelos/buscar")
    public String buscarVuelos(@RequestParam("origen") String origen,
                               @RequestParam("destino") String destino,
                               Model model) {

        List<Servicio> vuelosEncontrados = vueloService.buscarVuelos(origen, destino);

        // La lógica es la misma, pero ahora llama a métodos del servicio
        if (vuelosEncontrados.isEmpty()) {
            model.addAttribute("sugerencias", vueloService.obtenerSugerenciasDeVuelos());
        }

        model.addAttribute("vuelos", vuelosEncontrados);
        model.addAttribute("origen", origen);
        model.addAttribute("destino", destino);

        return "vuelos-resultados";
    }
}