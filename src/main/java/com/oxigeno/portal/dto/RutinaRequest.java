package com.oxigeno.portal.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RutinaRequest {

    private String nombre;
    private String objetivo;
    private String nivel;
    private String dias;
    private String observaciones;
    private Boolean activa;
    private List<RutinaEjercicioRequest> ejercicios;
}