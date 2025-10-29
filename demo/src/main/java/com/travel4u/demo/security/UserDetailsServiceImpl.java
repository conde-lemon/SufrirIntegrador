package com.travel4u.demo.security;

import com.travel4u.demo.usuario.model.Usuario;
import com.travel4u.demo.usuario.repository.IUsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioDAO usuarioDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscamos al usuario por su email
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // Verificar si el usuario está activo
        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + email);
        }

        // Crear el objeto UserDetails con la información del usuario
        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword()) // Ya está encriptada
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol())))
                .build();
    }
}