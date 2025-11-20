package com.travel4u.demo.reserva.controller;

import com.travel4u.demo.reserva.model.Detalle_Reserva;
import com.travel4u.demo.reserva.model.Equipaje;
import com.travel4u.demo.reserva.model.Reserva;
import com.travel4u.demo.reserva.model.Reserva_Equipaje;
import com.travel4u.demo.reserva.repository.IDetalleReservaDAO;
import com.travel4u.demo.reserva.repository.IEquipajeDAO;
import com.travel4u.demo.reserva.repository.IReservaDAO;
import com.travel4u.demo.reserva.repository.IReservaEquipajeDAO;
import com.travel4u.demo.reserva.service.FlightInfoGeneratorService;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping
public class ReservaController {

    private final IReservaDAO reservaDAO;
    private final IUsuarioDAO usuarioDAO;
    private final IEquipajeDAO equipajeDAO;
    private final IReservaEquipajeDAO reservaEquipajeDAO;
    private final IDetalleReservaDAO detalleReservaDAO;
    private final com.travel4u.demo.servicio.repository.IServicioDAO servicioDAO;
    private final FlightInfoGeneratorService flightInfoGenerator;

    public ReservaController(IReservaDAO reservaDAO, IUsuarioDAO usuarioDAO, IEquipajeDAO equipajeDAO, IReservaEquipajeDAO reservaEquipajeDAO, IDetalleReservaDAO detalleReservaDAO, com.travel4u.demo.servicio.repository.IServicioDAO servicioDAO, FlightInfoGeneratorService flightInfoGenerator) {
        this.reservaDAO = reservaDAO;
        this.usuarioDAO = usuarioDAO;
        this.equipajeDAO = equipajeDAO;
        this.reservaEquipajeDAO = reservaEquipajeDAO;
        this.detalleReservaDAO = detalleReservaDAO;
        this.servicioDAO = servicioDAO;
        this.flightInfoGenerator = flightInfoGenerator;
    }

    @GetMapping("/reservas")
    public String showMisReservasPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        try {
            Usuario usuario = usuarioDAO.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            List<Reserva> reservasDelUsuario = reservaDAO.findByUsuarioOrderByCreatedAtDesc(usuario);
            model.addAttribute("reservas", reservasDelUsuario);
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar reservas: " + e.getMessage());
            model.addAttribute("reservas", Collections.emptyList());
        }
        
        return "reservas";
    }

    @GetMapping("/reservas/{id}")
    public String showReservaDetallePage(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        try {
            Reserva reserva = reservaDAO.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

            if (!reserva.getUsuario().getEmail().equals(userDetails.getUsername())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permisos para ver esta reserva");
            }

            List<Reserva_Equipaje> equipajesDeLaReserva = reservaEquipajeDAO.findByReserva(reserva);
            model.addAttribute("reserva", reserva);
            model.addAttribute("equipajes", equipajesDeLaReserva);

            return "reserva-detalle";
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar detalle de reserva: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/reservar/servicio/{idServicio}")
    public String showAsientosPageForService(@PathVariable("idServicio") Long idServicio, Model model) {
        System.out.println("[DEBUG] Iniciando reserva para servicio ID: " + idServicio);
        
        try {
            com.travel4u.demo.servicio.model.Servicio servicio = servicioDAO.findById(idServicio)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
            
            model.addAttribute("servicio", servicio);
            model.addAttribute("reserva", new Reserva());
            
            List<Equipaje> tiposDeEquipaje = equipajeDAO.findAll();
            model.addAttribute("tiposDeEquipaje", tiposDeEquipaje);
            
            return "asientos";
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar servicio: " + e.getMessage());
            throw e;
        }
    }
    
    @GetMapping("/reservar/vuelo/{idVuelo}")
    public String showAsientosPage(@PathVariable("idVuelo") Long idVuelo, Model model) {
        return "redirect:/reservar/servicio/" + idVuelo;
    }
    
    @GetMapping("/reservar/bus/{idBus}")
    public String showAsientosBusPage(@PathVariable("idBus") Long idBus, Model model) {
        try {
            com.travel4u.demo.servicio.model.Servicio servicio = servicioDAO.findById(idBus)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
            
            model.addAttribute("servicio", servicio);
            List<Equipaje> tiposDeEquipaje = equipajeDAO.findAll();
            model.addAttribute("tiposDeEquipaje", tiposDeEquipaje);
            
            return "asientos-bus";
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar servicio de bus: " + e.getMessage());
            throw e;
        }
    }
    
    @GetMapping("/reservar/crucero/{idCrucero}")
    public String showAsientosCruceroPage(@PathVariable("idCrucero") Long idCrucero, Model model) {
        try {
            com.travel4u.demo.servicio.model.Servicio servicio = servicioDAO.findById(idCrucero)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado"));
            
            model.addAttribute("servicio", servicio);
            
            return "asientos-crucero";
        } catch (Exception e) {
            System.err.println("[ERROR] Error al cargar servicio de crucero: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/reservar/crear")
    public String crearReserva(
            @RequestParam("idServicio") Long idServicio,
            @RequestParam("asientoSeleccionado") String asientoSeleccionado,
            @RequestParam(value = "equipajeIds", required = false) List<Integer> equipajeIds,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("documento") String documento,
            @RequestParam("fechaNacimiento") String fechaNacimiento,
            @RequestParam("sexo") String sexo,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        System.out.println("[DEBUG] Creando reserva - Usuario: " + userDetails.getUsername());
        System.out.println("[DEBUG] Parámetros: idServicio=" + idServicio + ", asiento=" + asientoSeleccionado);
        System.out.println("[DEBUG] Datos pasajero: " + nombre + " " + apellido);
        
        if (idServicio == null) {
            System.out.println("[ERROR] ID de servicio es null");
            redirectAttributes.addFlashAttribute("error", "ID de servicio requerido");
            return "redirect:/";
        }
        
        if (asientoSeleccionado == null || asientoSeleccionado.trim().isEmpty()) {
            System.out.println("[ERROR] Asiento no seleccionado: '" + asientoSeleccionado + "'");
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar un asiento");
            return "redirect:/reservar/servicio/" + idServicio;
        }
        
        try {
            Usuario usuario = usuarioDAO.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            com.travel4u.demo.servicio.model.Servicio servicio = servicioDAO.findById(idServicio)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
            
            BigDecimal precioTotal = servicio.getPrecioBase();
            
            if (equipajeIds != null && !equipajeIds.isEmpty()) {
                for (Integer equipajeId : equipajeIds) {
                    Equipaje equipaje = equipajeDAO.findById(equipajeId)
                            .orElseThrow(() -> new RuntimeException("Equipaje no encontrado"));
                    precioTotal = precioTotal.add(equipaje.getPrecio());
                }
            }
            
            Reserva reserva = new Reserva();
            reserva.setUsuario(usuario);
            reserva.setEstado("CONFIRMADA");
            reserva.setMoneda("PEN");
            reserva.setTotal(precioTotal);
            
            Reserva reservaGuardada = reservaDAO.save(reserva);
            
            Detalle_Reserva detalleReserva = new Detalle_Reserva();
            detalleReserva.setReserva(reservaGuardada);
            detalleReserva.setServicio(servicio);
            detalleReserva.setCantidad(1);
            detalleReserva.setPrecioUnitario(servicio.getPrecioBase());
            detalleReserva.setSubtotal(servicio.getPrecioBase());
            detalleReserva.setNombre(nombre);
            detalleReserva.setApellido(apellido);
            detalleReserva.setDocumento(documento);
            detalleReserva.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
            detalleReserva.setSexo(sexo);
            detalleReserva.setCorreo(correo);
            detalleReserva.setTelefono(telefono);
            detalleReserva.setAsientoSeleccionado(asientoSeleccionado);
            
            // Generar información técnica automáticamente
            String tipoServicio = servicio.getTipoServicio();
            detalleReserva.setPuertaAbordaje(flightInfoGenerator.generateBoardingGate(tipoServicio));
            detalleReserva.setFechaSalida(flightInfoGenerator.generateDepartureDate());
            detalleReserva.setHoraSalida(flightInfoGenerator.generateDepartureTime(tipoServicio));
            
            System.out.println("[DEBUG] Información generada - Puerta: " + detalleReserva.getPuertaAbordaje() + 
                             ", Fecha: " + detalleReserva.getFechaSalida() + 
                             ", Hora: " + detalleReserva.getHoraSalida());
            
            detalleReservaDAO.save(detalleReserva);
            
            if (equipajeIds != null && !equipajeIds.isEmpty()) {
                for (Integer equipajeId : equipajeIds) {
                    Equipaje equipaje = equipajeDAO.findById(equipajeId).orElse(null);
                    if (equipaje != null) {
                        Reserva_Equipaje reservaEquipaje = new Reserva_Equipaje();
                        reservaEquipaje.setReserva(reservaGuardada);
                        reservaEquipaje.setEquipaje(equipaje);
                        reservaEquipaje.setCantidad(1);
                        reservaEquipaje.setPrecioUnitario(equipaje.getPrecio());
                        reservaEquipaje.setSubtotal(equipaje.getPrecio());
                        reservaEquipajeDAO.save(reservaEquipaje);
                    }
                }
            }
            
            System.out.println("[DEBUG] Reserva creada exitosamente con ID: " + reservaGuardada.getIdReserva());
            System.out.println("[DEBUG] Redirigiendo a: /pago/procesar/" + reservaGuardada.getIdReserva());
            
            redirectAttributes.addFlashAttribute("mensaje", "Reserva creada exitosamente");
            return "redirect:/pago/procesar/" + reservaGuardada.getIdReserva();
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al crear reserva: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al crear la reserva: " + e.getMessage());
            return "redirect:/reservar/servicio/" + idServicio;
        }
    }

    @GetMapping("/api/reservas/{id}")
    @ResponseBody
    public Reserva getReservaDetalleJson(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        Reserva reserva = reservaDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

        if (!reserva.getUsuario().getEmail().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        return reserva;
    }
}