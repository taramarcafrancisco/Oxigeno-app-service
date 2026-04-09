package com.oxigeno.portal.controller;

import com.oxigeno.portal.dto.RutinaRequest;
import com.oxigeno.portal.dto.RutinaResponse;
import com.oxigeno.portal.services.RutinaService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutinas")
@CrossOrigin
public class RutinaController {

    private final RutinaService rutinaService;

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
    public RutinaResponse miRutina(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return rutinaService.obtenerRutinaActivaPorEmail(authentication.getName());
    }
}