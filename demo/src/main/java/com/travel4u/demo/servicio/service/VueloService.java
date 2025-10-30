package com.travel4u.demo.servicio; // O donde prefieras poner tus servicios

import com.travel4u.demo.servicio.model.Servicio;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VueloService {

    private final IServicioDAO servicioDAO;

    public VueloService(IServicioDAO servicioDAO) {
        this.servicioDAO = servicioDAO;
    }

    public List<Servicio> buscarVuelos(String origen, String destino) {
        return servicioDAO.findByTipoServicioAndOrigenAndDestinoAndActivoTrue("VUELO", origen, destino);
    }

    public List<Servicio> obtenerSugerenciasDeVuelos() {
        return servicioDAO.findTop5ByTipoServicioAndActivoTrue("VUELO");
    }

    public List<Servicio> obtenerTodosLosVuelos() {
        return servicioDAO.findByTipoServicioAndActivoTrue("VUELO");
    }
}