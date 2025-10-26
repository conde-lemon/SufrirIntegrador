package com.travel4u.demo.usuario.service;

import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> buscarUsuarios(String criterio, boolean soloActivos) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return soloActivos ? usuarioRepository.findByActivoTrue() : usuarioRepository.findAll();
        }
        return soloActivos ?
                usuarioRepository.buscarUsuariosActivos(criterio) :
                usuarioRepository.buscarTodosUsuarios(criterio);
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, String nombres, String apellidos,
                                     String email, String telefono, String rol, Boolean activo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (nombres != null && !nombres.trim().isEmpty()) {
                usuario.setNombres(nombres);
            }

            if (apellidos != null && !apellidos.trim().isEmpty()) {
                usuario.setApellidos(apellidos);
            }

            if (email != null && !email.trim().isEmpty()) {
                // Verificar que el email no esté en uso por otro usuario
                if (usuarioRepository.existsByEmailAndIdUsuarioNot(email, id)) {
                    throw new RuntimeException("El email ya está en uso por otro usuario");
                }
                usuario.setEmail(email);
            }

            if (telefono != null) {
                usuario.setTelefono(telefono);
            }

            if (rol != null && !rol.trim().isEmpty()) {
                usuario.setRol(rol);
            }

            if (activo != null) {
                usuario.setActivo(activo);
            }

            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + id);
    }

    @Transactional
    public boolean eliminarUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            // Borrado lógico en lugar de físico
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    // Eliminación física (solo para administradores)
    @Transactional
    public boolean eliminarUsuarioFisicamente(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
