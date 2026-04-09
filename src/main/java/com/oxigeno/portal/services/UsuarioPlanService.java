package com.oxigeno.portal.services;

import com.oxigeno.portal.entity.UsuarioPlan;

import java.time.LocalDate;
import java.util.List;

public interface UsuarioPlanService {

    UsuarioPlan asignarPlan(Integer usuarioId, Integer planId, LocalDate fechaInicio, LocalDate fechaFin);

    List<UsuarioPlan> listarPorUsuario(Integer usuarioId);

    UsuarioPlan obtenerActivoPorUsuario(Integer usuarioId);
}