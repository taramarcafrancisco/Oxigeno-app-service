package com.oxigeno.portal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RutinaEjercicioRequest {

    private String ejercicio;
    private Integer series;
    private String repeticiones;
    private String descanso;
    private Integer orden;
}