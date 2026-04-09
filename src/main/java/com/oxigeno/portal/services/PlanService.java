package com.oxigeno.portal.services;

import com.oxigeno.portal.entity.Plan;

import java.util.List;

public interface PlanService {

    List<Plan> listar();

    Plan obtener(Integer id);

    Plan crear(Plan plan);

    Plan actualizar(Integer id, Plan plan);

    void eliminar(Integer id);
}