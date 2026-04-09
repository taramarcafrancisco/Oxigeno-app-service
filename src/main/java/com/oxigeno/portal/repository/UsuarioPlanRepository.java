package com.oxigeno.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oxigeno.portal.entity.UsuarioPlan;

import java.util.List;
import java.util.Optional;

public interface UsuarioPlanRepository extends JpaRepository<UsuarioPlan, Integer> {

    List<UsuarioPlan> findByUsuario_IdUsuarioOrderByFechaInicioDesc(Integer idUsuario);

    Optional<UsuarioPlan> findFirstByUsuario_IdUsuarioAndActivoTrueOrderByFechaFinAsc(Integer idUsuario);
}