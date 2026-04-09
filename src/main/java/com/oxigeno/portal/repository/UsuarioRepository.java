package com.oxigeno.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oxigeno.portal.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
    Usuario findByEmail(String email);
}