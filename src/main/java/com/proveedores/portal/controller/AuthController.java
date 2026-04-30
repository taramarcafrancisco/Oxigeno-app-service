package com.proveedores.portal.controller;

import com.proveedores.portal.entity.Usuario;
import com.proveedores.portal.repository.UsuarioRepository;
import com.proveedores.portal.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.email == null || request.password == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioRepository.findByEmail(request.email.trim());
        if (usuario == null || !usuario.isActivo() || !passwordValida(request.password, usuario.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        LoginResponse response = new LoginResponse();
        response.token = jwtUtil.generarToken(usuario.getId(), usuario.getEmail());
        response.usuario = UsuarioDto.fromEntity(usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioDto> me(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName());
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(UsuarioDto.fromEntity(usuario));
    }

    private boolean passwordValida(String raw, String stored) {
        if (stored == null) {
            return false;
        }
        try {
            return passwordEncoder.matches(raw, stored) || raw.equals(stored);
        } catch (IllegalArgumentException ex) {
            return raw.equals(stored);
        }
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class LoginResponse {
        public String token;
        public UsuarioDto usuario;
    }

    public static class UsuarioDto {
        public Long id;
        public String nombre;
        public String email;
        public boolean activo;
        public List<String> roles;

        static UsuarioDto fromEntity(Usuario usuario) {
            UsuarioDto dto = new UsuarioDto();
            dto.id = usuario.getId();
            dto.nombre = usuario.getNombre();
            dto.email = usuario.getEmail();
            dto.activo = usuario.isActivo();
            dto.roles = usuario.getRoles() == null
                ? Collections.<String>emptyList()
                : usuario.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toList());
            return dto;
        }
    }
}
