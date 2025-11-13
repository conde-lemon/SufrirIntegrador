package com.travel4u.demo.reserva.service;

import com.travel4u.demo.reserva.model.Pago;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.repository.IPagoDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);
    private final IPagoDAO pagoDAO;

    public PagoService(IPagoDAO pagoDAO) {
        this.pagoDAO = pagoDAO;
    }

    public Pago procesarPagoPayPal(Reserva reserva, String emailPayPal) {
        logger.info("Procesando pago PayPal para reserva ID: {}", reserva.getIdReserva());
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMonto(reserva.getTotal());
        pago.setMetodoPago("PayPal");
        pago.setEstadoPago("Completado");
        pago.setFechaPago(LocalDateTime.now());
        pago.setReferenciaPago("PP-" + UUID.randomUUID().toString().substring(0, 18).toUpperCase());

        return pagoDAO.save(pago);
    }

    public Pago procesarPagoTarjeta(Reserva reserva, String numeroTarjeta) {
        logger.info("Procesando pago con tarjeta para reserva ID: {}", reserva.getIdReserva());
        
        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMonto(reserva.getTotal());
        pago.setMetodoPago("Tarjeta de Crédito");
        pago.setEstadoPago("Completado");
        pago.setFechaPago(LocalDateTime.now());
        pago.setReferenciaPago("TC-" + System.currentTimeMillis());

        return pagoDAO.save(pago);
    }
}
