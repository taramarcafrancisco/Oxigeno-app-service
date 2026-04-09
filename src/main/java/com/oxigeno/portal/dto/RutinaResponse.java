package com.oxigeno.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RutinaResponse {

    private Integer idRutina;
    private String nombre;
    private String objetivo;
    private String nivel;
    private String dias;
    private String observaciones;
    private LocalDate fechaAsignacion;
    private Boolean activa;
    private Integer idUsuario;
    private List<RutinaEjercicioResponse> ejercicios;
}