package com.oxigeno.portal.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.oxigeno.portal.entity.Usuario;
import com.oxigeno.portal.repository.UsuarioRepository;

@Service
public class DbUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  public DbUserDetailsService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Usuario u = usuarioRepository.findByEmail(email);

    if (u == null) {
      throw new UsernameNotFoundException("Usuario no encontrado");
    }

    if (u.getEstado() != null && u.getEstado() == 0) {
      throw new DisabledException("Usuario deshabilitado");
    }

    List<SimpleGrantedAuthority> authorities =
        (u.getRoles() == null
            ? new ArrayList<SimpleGrantedAuthority>()
            : u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getNombre()))
                .collect(Collectors.toList()));

    return org.springframework.security.core.userdetails.User
        .withUsername(u.getEmail())
        .password(u.getPassword())
        .authorities(authorities)
        .build();
  }
}