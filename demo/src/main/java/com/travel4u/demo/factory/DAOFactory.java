package com.travel4u.demo.factory;

import com.travel4u.demo.oferta.repository.IOfertaDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;

/**
 * Interfaz Abstract Factory para obtener los diferentes DAOs.
 * Esto permite desacoplar los servicios de las implementaciones concretas de los DAOs.
 */
public interface DAOFactory {
    IUsuarioDAO getUsuarioDAO();
    IReservaDAO getReservaDAO();
    IServicioDAO getServicioDAO();
    IOfertaDAO getOfertaDAO();
}