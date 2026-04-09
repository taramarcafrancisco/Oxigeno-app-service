package com.oxigeno.portal.controller;

import com.oxigeno.portal.entity.Plan;
import com.oxigeno.portal.services.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@CrossOrigin
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public List<Plan> listar() {
        return planService.listar();
    }

    @GetMapping("/{id}")
    public Plan obtener(@PathVariable Integer id) {
        return planService.obtener(id);
    }

    @PostMapping
    public Plan crear(@RequestBody Plan plan) {
        return planService.crear(plan);
    }

    @PutMapping("/{id}")
    public Plan actualizar(@PathVariable Integer id, @RequestBody Plan plan) {
        return planService.actualizar(id, plan);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        planService.eliminar(id);
    }
}