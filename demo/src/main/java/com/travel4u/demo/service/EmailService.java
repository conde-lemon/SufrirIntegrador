package com.travel4u.demo.service;

import com.travel4u.demo.reserva.model.Pago;
import com.travel4u.demo.reserva.model.Reserva;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void enviarConfirmacionPago(Reserva reserva, Pago pago) {
        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("Para: " + reserva.getUsuario().getEmail());
        System.out.println("Asunto: Confirmación de Pago - Travel4U");
        System.out.println("Reserva: TFU-" + reserva.getIdReserva());
        System.out.println("Monto: " + reserva.getMoneda() + " " + reserva.getTotal());
        System.out.println("Referencia: " + pago.getReferenciaPago());
        System.out.println("✅ Email de confirmación enviado exitosamente");
    }

    public void enviarBoletaElectronica(Reserva reserva) {
        System.out.println("📋 BOLETA ELECTRÓNICA ENVIADA:");
        System.out.println("Para: " + reserva.getUsuario().getEmail());
        System.out.println("Reserva: TFU-" + reserva.getIdReserva());
        System.out.println("✅ Boleta enviada por email");
    }
}