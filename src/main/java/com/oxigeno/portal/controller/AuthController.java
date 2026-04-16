package com.oxigeno.portal.controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oxigeno.portal.entity.Usuario;
import com.oxigeno.portal.repository.UsuarioRepository;
import com.oxigeno.portal.security.JwtUtil;

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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {

        if (req == null || req.email == null || req.password == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario u = usuarioRepository.findByEmail(req.email.trim());
        if (u == null) {
            return ResponseEntity.status(401).build();
        }

        if (u.getEstado() != null && u.getEstado() == 0) {
            return ResponseEntity.status(403).build();
        }

        // Cuando migres a hash, reemplazá esto por passwordEncoder.matches(...)
        if (!req.password.equals(u.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtUtil.generarToken(u.getIdUsuario(), u.getEmail());

        LoginResponse out = new LoginResponse();
        out.token = token;

        LoginResponse.UserDto dto = new LoginResponse.UserDto();
        dto.id = u.getIdUsuario();
        dto.email = u.getEmail();
        dto.estado = u.getEstado();
        dto.roles = (u.getRoles() == null)
        		 ? Collections.<String>emptyList()
                : u.getRoles().stream()
                    .map(r -> r.getNombre())
                    .collect(Collectors.toList());

        out.user = dto;

        return ResponseEntity.ok(out);
    }

    @GetMapping("/me")
    public ResponseEntity<UserFullDto> me(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        Usuario u = usuarioRepository.findByEmail(auth.getName().trim());
        if (u == null) {
            return ResponseEntity.status(401).build();
        }

        UserFullDto dto = new UserFullDto();
        dto.id = u.getIdUsuario();
        dto.email = u.getEmail();
        dto.estado = u.getEstado();

        dto.roles = (u.getRoles() == null)
        		 ? Collections.<String>emptyList()
                : u.getRoles().stream()
                    .map(r -> r.getNombre())
                    .collect(Collectors.toList());

        dto.nombre = u.getNombre();
        dto.apellido = u.getApellido();
        dto.telefono = u.getTel();

        dto.planes = (u.getPlanes() == null)
                ?   Collections.<PlanDto>emptyList()
                : u.getPlanes().stream()
                    .filter(up -> Boolean.TRUE.equals(up.getActivo()))
                    .filter(up -> up.getPlan() != null)
                    .map(up -> {
                        PlanDto p = new PlanDto();
                        p.id = up.getPlan().getIdPlan();
                        p.nombre = up.getPlan().getNombre();
//                        p.tipoProducto = up.getPlan().getTipoProducto() != null
//                                ? up.getPlan().getTipoProducto().name()
//                                : null;
                        p.fechaInicio = up.getFechaInicio() != null
                                ? up.getFechaInicio().toString()
                                : null;
                        p.fechaFin = up.getFechaFin() != null
                                ? up.getFechaFin().toString()
                                : null;
                        p.activo = up.getActivo();
                        return p;
                    })
                    .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class LoginResponse {
        public String token;
        public UserDto user;

        public static class UserDto {
            public Integer id;
            public String email;
            public Integer estado;
            public List<String> roles;
        }
    }

    public static class UserFullDto {
        public Integer id;
        public String email;
        public Integer estado;
        public List<String> roles;

        public String nombre;
        public String apellido;
        public String telefono;
        public List<PlanDto> planes;
    }

    public static class PlanDto {
        public Integer id;
        public String nombre;
        public String tipoProducto;
        public String fechaInicio;
        public String fechaFin;
        public Boolean activo;
    }
    
    public static class ChangePasswordRequest {
        public String currentPassword;
        public String newPassword;
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest req,
            Authentication auth
    ) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }

        if (req == null || req.currentPassword == null || req.newPassword == null) {
            return ResponseEntity.badRequest().body("Datos incompletos");
        }

        Usuario u = usuarioRepository.findByEmail(auth.getName().trim());
        if (u == null) {
            return ResponseEntity.status(401).build();
        }

        // VALIDACIÓN ACTUAL SEGÚN TU LOGIN
        // OJO: hoy estás comparando texto plano, no hash
        if (!req.currentPassword.equals(u.getPassword())) {
            return ResponseEntity.badRequest().body("La contraseña actual es incorrecta");
        }

        u.setPassword(req.newPassword);
        usuarioRepository.save(u);

        return ResponseEntity.ok().body("Contraseña actualizada correctamente");
    }
    
@PutMapping("/me")
public ResponseEntity<?> updateMe(
        @RequestBody UpdateUserRequest req,
        Authentication auth
) {
    if (auth == null || auth.getName() == null) {
        return ResponseEntity.status(401).build();
    }

    if (req == null) {
        return ResponseEntity.badRequest().body("Datos incompletos");
    }

    Usuario u = usuarioRepository.findByEmail(auth.getName().trim());
    if (u == null) {
        return ResponseEntity.status(401).build();
    }

    u.setNombre(req.nombre);
    u.setApellido(req.apellido);
    u.setRazonSocial(req.razonSocial);
    u.setCuit(req.cuit);
    u.setDireccion(req.direccion);
    u.setTel(req.telefono);

    usuarioRepository.save(u);

    return ResponseEntity.ok().body("Datos actualizados correctamente");
}
    
    public static class UpdateUserRequest {
        public String nombre;
        public String apellido;
        public String razonSocial;
        public String cuit;
        public String direccion;
        public String telefono;
    }
}
