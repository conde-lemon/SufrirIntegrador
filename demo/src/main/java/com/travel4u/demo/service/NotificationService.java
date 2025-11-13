package com.travel4u.demo.service;

import com.travel4u.demo.reserva.model.Reserva;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void enviarNotificacionReserva(Reserva reserva, String tipoNotificacion) {
        switch (tipoNotificacion.toUpperCase()) {
            case "CONFIRMACION":
                System.out.println("🔔 NOTIFICACIÓN: Reserva TFU-" + reserva.getIdReserva() + " confirmada");
                break;
            case "RECORDATORIO":
                System.out.println("⏰ RECORDATORIO: Tu viaje es en 24 horas - TFU-" + reserva.getIdReserva());
                break;
            case "CANCELACION":
                System.out.println("❌ CANCELACIÓN: Reserva TFU-" + reserva.getIdReserva() + " cancelada");
                break;
            default:
                System.out.println("📢 NOTIFICACIÓN: Actualización en reserva TFU-" + reserva.getIdReserva());
        }
    }

    public void programarRecordatorioViaje(Reserva reserva) {
        System.out.println("📅 RECORDATORIO PROGRAMADO:");
        System.out.println("Reserva: TFU-" + reserva.getIdReserva());
        System.out.println("Fecha de viaje: " + reserva.getFechaInicio());
        System.out.println("Se enviará recordatorio 24h antes del viaje");
    }
}