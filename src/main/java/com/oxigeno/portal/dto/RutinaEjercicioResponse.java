package com.oxigeno.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RutinaEjercicioResponse {

    private Integer idRutinaEjercicio;
    private String ejercicio;
    private Integer series;
    private String repeticiones;
    private String descanso;
    private Integer orden;
}