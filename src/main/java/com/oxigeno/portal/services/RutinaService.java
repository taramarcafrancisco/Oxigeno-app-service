package com.oxigeno.portal.services;

import com.oxigeno.portal.dto.RutinaRequest;
import com.oxigeno.portal.dto.RutinaResponse;
import java.util.List;

public interface RutinaService {

    RutinaResponse crearRutina(Integer usuarioId, RutinaRequest request);

    RutinaResponse actualizarRutina(Integer rutinaId, RutinaRequest request);

    List<RutinaResponse> listarPorUsuario(Integer usuarioId);

    RutinaResponse obtenerRutinaActivaPorUsuario(Integer usuarioId);

    RutinaResponse obtenerRutinaActivaPorEmail(String email);

	RutinaResponse obtenerRutinaCompletaPorEmail(String email);
}