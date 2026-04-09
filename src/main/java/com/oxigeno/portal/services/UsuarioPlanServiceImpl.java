package com.oxigeno.portal.services;

import com.oxigeno.portal.entity.Plan;
import com.oxigeno.portal.entity.Usuario;
import com.oxigeno.portal.entity.UsuarioPlan;
import com.oxigeno.portal.repository.PlanRepository;
import com.oxigeno.portal.repository.UsuarioPlanRepository;
import com.oxigeno.portal.repository.UsuarioRepository;
 
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioPlanServiceImpl implements UsuarioPlanService {

    private final UsuarioPlanRepository usuarioPlanRepository;
    private final UsuarioRepository usuarioRepository;
    private final PlanRepository planRepository;

    public UsuarioPlanServiceImpl(
            UsuarioPlanRepository usuarioPlanRepository,
            UsuarioRepository usuarioRepository,
            PlanRepository planRepository
    ) {
        this.usuarioPlanRepository = usuarioPlanRepository;
        this.usuarioRepository = usuarioRepository;
        this.planRepository = planRepository;
    }

    @Override
    public UsuarioPlan asignarPlan(Integer usuarioId, Integer planId, LocalDate fechaInicio, LocalDate fechaFin) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        if (fechaInicio == null) {
            fechaInicio = LocalDate.now();
        }

        if (fechaFin == null) {
            fechaFin = fechaInicio.plusDays(plan.getDuracionDias());
        }

        UsuarioPlan usuarioPlan = UsuarioPlan.builder()
                .usuario(usuario)
                .plan(plan)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .activo(Boolean.TRUE)
                .build();

        return usuarioPlanRepository.save(usuarioPlan);
    }

    @Override
    public List<UsuarioPlan> listarPorUsuario(Integer usuarioId) {
        return usuarioPlanRepository.findByUsuario_IdUsuarioOrderByFechaInicioDesc(usuarioId);
    }

    @Override
    public UsuarioPlan obtenerActivoPorUsuario(Integer usuarioId) {
        return usuarioPlanRepository
                .findFirstByUsuario_IdUsuarioAndActivoTrueOrderByFechaFinAsc(usuarioId)
                .orElse(null);
    }
}