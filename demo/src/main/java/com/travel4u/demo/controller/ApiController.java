package com.travel4u.demo.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import com.travel4u.demo.servicio.model.Servicio;
import com.travel4u.demo.servicio.repository.IServicioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Autowired
    private IServicioDAO servicioDAO;

    @GetMapping("/usuarios")
    public ResponseEntity<List<Map<String, Object>>> obtenerUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.findAll();
            List<Map<String, Object>> usuariosSimplificados = usuarios.stream()
                .map(this::convertirUsuario)
                .collect(Collectors.toList());
            return ResponseEntity.ok(usuariosSimplificados);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/servicios")
    public ResponseEntity<List<Map<String, Object>>> obtenerServicios() {
        try {
            List<Servicio> servicios = servicioDAO.findAll();
            List<Map<String, Object>> serviciosSimplificados = servicios.stream()
                .map(this::convertirServicio)
                .collect(Collectors.toList());
            return ResponseEntity.ok(serviciosSimplificados);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    private Map<String, Object> convertirUsuario(Usuario usuario) {
        Map<String, Object> map = new HashMap<>();
        map.put("idUsuario", usuario.getIdUsuario());
        map.put("nombres", usuario.getNombres());
        map.put("apellidos", usuario.getApellidos());
        map.put("email", usuario.getEmail());
        map.put("telefono", usuario.getTelefono());
        map.put("rol", usuario.getRol());
        map.put("activo", usuario.isActivo());
        map.put("fechaRegistro", usuario.getFechaRegistro());
        return map;
    }

    private Map<String, Object> convertirServicio(Servicio servicio) {
        Map<String, Object> map = new HashMap<>();
        map.put("idServicio", servicio.getIdServicio());
        map.put("nombre", servicio.getNombre());
        map.put("origen", servicio.getOrigen());
        map.put("destino", servicio.getDestino());
        map.put("precioBase", servicio.getPrecioBase());
        map.put("tipoServicio", servicio.getTipoServicio());
        map.put("activo", servicio.isActivo());
        return map;
    }
}