package com.oxigeno.portal.repository;

import com.oxigeno.portal.entity.RutinaEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaEjercicioRepository extends JpaRepository<RutinaEjercicio, Integer> {

	  List<RutinaEjercicio> findByRutina_IdRutinaOrderByOrdenAsc(Integer idRutina);

}