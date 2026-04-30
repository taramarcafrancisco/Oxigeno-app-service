package com.proveedores.portal.security;

import com.proveedores.portal.entity.Usuario;
import com.proveedores.portal.repository.UsuarioRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public DbUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        if (!usuario.isActivo()) {
            throw new DisabledException("Usuario deshabilitado");
        }

        List<SimpleGrantedAuthority> authorities = usuario.getRoles() == null
            ? new ArrayList<SimpleGrantedAuthority>()
            : usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
            .withUsername(usuario.getEmail())
            .password(usuario.getPassword())
            .authorities(authorities)
            .build();
    }
}
