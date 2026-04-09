package com.oxigeno.portal.controller;

 
import com.oxigeno.portal.entity.Usuario;
import com.oxigeno.portal.repository.UsuarioRepository;
 
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final UsuarioRepository usuarioRepository;

	public AdminController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;

	}

	@GetMapping("/usuarios")
	public List<Usuario> getAllUsuarios() {
		return usuarioRepository.findAll();
	}

}
