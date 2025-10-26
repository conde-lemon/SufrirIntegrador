package com.travel4u.demo.usuario.controller;

import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("vistadmin")

public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    // Habilita CORS para cada endpoint
    @GetMapping
    @CrossOrigin(origins = "http://localhost:8081")
    public String mostrarUsuarios(@RequestParam(required = false) String search, Model model) {
        System.out.println("=== ACCEDIENDO A VISTADMIN ===");
        List<Usuario> usuarios = usuarioService.buscarUsuarios(search, false);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("search", search);
        return "admin/usuarios";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean eliminado = usuarioService.eliminarUsuario(id);
            response.put("success", eliminado);
            response.put("message", eliminado ? "Usuario eliminado correctamente" : "Usuario no encontrado");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar usuario: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(
            @PathVariable Long id,
            @RequestParam(required = false) String nombres,
            @RequestParam(required = false) String apellidos,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) Boolean activo) {

        Map<String, Object> response = new HashMap<>();
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(
                    id, nombres, apellidos, email, telefono, rol, activo);

            response.put("success", true);
            response.put("message", "Usuario actualizado correctamente");
            response.put("usuario", usuarioActualizado);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar usuario: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/datos")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:8081")
    public ResponseEntity<Map<String, Object>> obtenerDatosUsuario(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            response.put("success", true);
            response.put("usuario", Map.of(
                    "id", usuario.getIdUsuario(),
                    "nombres", usuario.getNombres(),
                    "apellidos", usuario.getApellidos(),
                    "email", usuario.getEmail(),
                    "telefono", usuario.getTelefono(),
                    "rol", usuario.getRol(),
                    "activo", usuario.isActivo()
            ));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    // metodo de verificacion
    @GetMapping("/test")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:8081")
    public String test() {
        return "{\"status\": \"ok\", \"message\": \"UsuarioController funcionando\"}";
    }
}
