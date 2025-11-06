package com.travel4u.demo.controller;

import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/vistadmin")
public class VistaAdminController {

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> desactivarUsuario(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.findById(id);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                usuario.setActivo(false);
                usuarioDAO.save(usuario);
                
                response.put("success", true);
                response.put("message", "Usuario desactivado correctamente");
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al desactivar usuario: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/datos")
    public ResponseEntity<Map<String, Object>> obtenerDatosUsuario(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.findById(id);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                Map<String, Object> datosUsuario = new HashMap<>();
                datosUsuario.put("id", usuario.getIdUsuario());
                datosUsuario.put("nombres", usuario.getNombres());
                datosUsuario.put("apellidos", usuario.getApellidos());
                datosUsuario.put("email", usuario.getEmail());
                datosUsuario.put("telefono", usuario.getTelefono());
                datosUsuario.put("rol", usuario.getRol());
                datosUsuario.put("activo", usuario.isActivo());
                
                response.put("success", true);
                response.put("usuario", datosUsuario);
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener datos: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(
            @PathVariable Long id,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String rol,
            @RequestParam boolean activo) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<Usuario> usuarioOpt = usuarioDAO.findById(id);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                usuario.setNombres(nombres);
                usuario.setApellidos(apellidos);
                usuario.setEmail(email);
                usuario.setTelefono(telefono);
                usuario.setRol(rol);
                usuario.setActivo(activo);
                
                usuarioDAO.save(usuario);
                
                response.put("success", true);
                response.put("message", "Usuario actualizado correctamente");
            } else {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar usuario: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}