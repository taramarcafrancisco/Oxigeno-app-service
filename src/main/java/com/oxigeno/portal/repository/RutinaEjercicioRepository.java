package com.oxigeno.portal.repository;

import com.oxigeno.portal.entity.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RutinaEjercicioRepository extends JpaRepository<Rutina, Integer> {

    List<Rutina> findByUsuario_IdUsuarioOrderByFechaAsignacionDesc(Integer idUsuario);

    Optional<Rutina> findFirstByUsuario_IdUsuarioAndActivaTrueOrderByFechaAsignacionDesc(Integer idUsuario);

    Optional<Rutina> findFirstByUsuario_EmailAndActivaTrueOrderByFechaAsignacionDesc(String email);
}