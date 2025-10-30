package com.travel4u.demo.servicio.controller;

import com.travel4u.demo.servicio.model.Servicio;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ServicioController {

    private final IServicioDAO servicioDAO;

    public ServicioController(IServicioDAO servicioDAO) {
        this.servicioDAO = servicioDAO;
    }

    @GetMapping("/vuelos")
    public String mostrarVuelos(Model model) {
        List<Servicio> vuelos = servicioDAO.findByTipoServicioAndActivoTrue("VUELO");
        model.addAttribute("servicios", vuelos);
        model.addAttribute("tipoServicio", "VUELO");
        model.addAttribute("titulo", "Vuelos Disponibles");
        return "servicios-resultados";
    }

    @GetMapping("/cruceros")
    public String mostrarCruceros(Model model) {
        List<Servicio> cruceros = servicioDAO.findByTipoServicioAndActivoTrue("CRUCERO");
        model.addAttribute("servicios", cruceros);
        model.addAttribute("tipoServicio", "CRUCERO");
        model.addAttribute("titulo", "Cruceros Disponibles");
        return "servicios-resultados";
    }

    @GetMapping("/bus")
    public String mostrarBuses(Model model) {
        List<Servicio> buses = servicioDAO.findByTipoServicioAndActivoTrue("BUS");
        model.addAttribute("servicios", buses);
        model.addAttribute("tipoServicio", "BUS");
        model.addAttribute("titulo", "Buses Disponibles");
        return "servicios-resultados";
    }

    @GetMapping("/servicios/buscar")
    public String buscarServicios(
            @RequestParam("tipo") String tipo,
            @RequestParam(value = "origen", required = false) String origen,
            @RequestParam(value = "destino", required = false) String destino,
            Model model) {

        List<Servicio> servicios;
        
        if (origen != null && destino != null && !origen.isEmpty() && !destino.isEmpty()) {
            servicios = servicioDAO.findByTipoServicioAndOrigenAndDestinoAndActivoTrue(tipo.toUpperCase(), origen, destino);
        } else {
            servicios = servicioDAO.findByTipoServicioAndActivoTrue(tipo.toUpperCase());
        }

        List<Servicio> sugerencias = List.of();
        if (servicios.isEmpty()) {
            sugerencias = servicioDAO.findTop5ByTipoServicioAndActivoTrue(tipo.toUpperCase());
        }

        model.addAttribute("servicios", servicios);
        model.addAttribute("sugerencias", sugerencias);
        model.addAttribute("tipoServicio", tipo.toUpperCase());
        model.addAttribute("titulo", getTituloByTipo(tipo));
        model.addAttribute("origen", origen);
        model.addAttribute("destino", destino);

        return "servicios-resultados";
    }

    @GetMapping("/vuelos/buscar")
    public String buscarVuelos(
            @RequestParam("origen") String origen,
            @RequestParam("destino") String destino,
            Model model) {
        return buscarServicios("VUELO", origen, destino, model);
    }

    private String getTituloByTipo(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "VUELO" -> "Vuelos";
            case "CRUCERO" -> "Cruceros";
            case "BUS" -> "Buses";
            default -> "Servicios";
        };
    }
}