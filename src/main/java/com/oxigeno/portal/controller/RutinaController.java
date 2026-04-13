package com.oxigeno.portal.controller;

import com.oxigeno.portal.dto.RutinaRequest;
import com.oxigeno.portal.dto.RutinaResponse;
import com.oxigeno.portal.entity.Rutina;
import com.oxigeno.portal.entity.RutinaEjercicio;
import com.oxigeno.portal.repository.RutinaEjercicioRepository;
import com.oxigeno.portal.repository.RutinaRepository;
import com.oxigeno.portal.services.RutinaService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutinas")
@CrossOrigin
public class RutinaController {

    private final RutinaService rutinaService;
	private RutinaRepository rutinaRepository;
	private RutinaEjercicioRepository rutinaEjercicioRepository;

    public RutinaController(RutinaService rutinaService) {
        this.rutinaService = rutinaService;
    }

    @PostMapping("/usuario/{usuarioId}")
    public RutinaResponse crear(@PathVariable Integer usuarioId,
                                @RequestBody RutinaRequest request) {
        return rutinaService.crearRutina(usuarioId, request);
    }

    @PutMapping("/{rutinaId}")
    public RutinaResponse actualizar(@PathVariable Integer rutinaId,
                                     @RequestBody RutinaRequest request) {
        return rutinaService.actualizarRutina(rutinaId, request);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<RutinaResponse> listarPorUsuario(@PathVariable Integer usuarioId) {
        return rutinaService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/usuario/{usuarioId}/activa")
    public RutinaResponse obtenerActiva(@PathVariable Integer usuarioId) {
        return rutinaService.obtenerRutinaActivaPorUsuario(usuarioId);
    }

    @GetMapping("/mia")
    public ResponseEntity<?> getMiRutina(Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }

        String email = authentication.getName();

        RutinaResponse rutina = rutinaService.obtenerRutinaCompletaPorEmail(email);

        return ResponseEntity.ok(rutina);
    }
}