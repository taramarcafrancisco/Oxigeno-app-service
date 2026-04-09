package com.oxigeno.portal.services ;

import com.oxigeno.portal.entity.Plan;
import com.oxigeno.portal.repository.PlanRepository;
 
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> listar() {
        return planRepository.findAll();
    }

    @Override
    public Plan obtener(Integer id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
    }

    @Override
    public Plan crear(Plan plan) {
        plan.setIdPlan(null);
        if (plan.getActivo() == null) {
            plan.setActivo(Boolean.TRUE);
        }
        if (plan.getAccesoLibre() == null) {
            plan.setAccesoLibre(Boolean.TRUE);
        }
        return planRepository.save(plan);
    }

    @Override
    public Plan actualizar(Integer id, Plan plan) {
        Plan actual = obtener(id);

        actual.setNombre(plan.getNombre());
        actual.setPrecio(plan.getPrecio());
        actual.setDescripcion(plan.getDescripcion());
        actual.setDuracionDias(plan.getDuracionDias());
        actual.setClasesPorSemana(plan.getClasesPorSemana());
        actual.setAccesoLibre(plan.getAccesoLibre());
        actual.setActivo(plan.getActivo());

        return planRepository.save(actual);
    }

    @Override
    public void eliminar(Integer id) {
        Plan actual = obtener(id);
        planRepository.delete(actual);
    }
}