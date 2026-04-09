package com.oxigeno.portal.controller;

import com.oxigeno.portal.dto.AsignarPlanRequest;
import com.oxigeno.portal.entity.UsuarioPlan;
import com.oxigeno.portal.services.UsuarioPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios-planes")
@CrossOrigin
public class UsuarioPlanController {

    private final UsuarioPlanService usuarioPlanService;

    public UsuarioPlanController(UsuarioPlanService usuarioPlanService) {
        this.usuarioPlanService = usuarioPlanService;
    }

    @PostMapping("/usuario/{usuarioId}")
    public UsuarioPlan asignarPlan(@PathVariable Integer usuarioId,
                                   @RequestBody AsignarPlanRequest request) {
        return usuarioPlanService.asignarPlan(
                usuarioId,
                request.getIdPlan(),
                request.getFechaInicio(),
                request.getFechaFin()
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<UsuarioPlan> listarPorUsuario(@PathVariable Integer usuarioId) {
        return usuarioPlanService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/usuario/{usuarioId}/activo")
    public UsuarioPlan obtenerActivo(@PathVariable Integer usuarioId) {
        return usuarioPlanService.obtenerActivoPorUsuario(usuarioId);
    }
}