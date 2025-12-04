package com.travel4u.demo.controller.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la respuesta de servicios en el API REST
 * Evita problemas de serialización con relaciones JPA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioDTO {
    private Long idServicio;
    private String tipoServicio;
    private String nombre;
    private String origen;
    private String destino;
    private String tags;
    private BigDecimal precioBase;
    private int disponibilidad;
    private String descripcion;
    private boolean activo;
    private LocalDateTime createdAt;

    // Info del proveedor (simplificada)
    private Integer idProveedor;
    private String nombreProveedor;
    private String tipoProveedor;
}

