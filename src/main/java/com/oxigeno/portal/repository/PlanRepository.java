package com.oxigeno.portal.repository;

import com.oxigeno.portal.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    List<Plan> findByActivoTrueOrderByNombreAsc();

    Optional<Plan> findByNombreIgnoreCase(String nombre);
}