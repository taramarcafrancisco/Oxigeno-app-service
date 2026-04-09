package com.oxigeno.portal.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AsignarPlanRequest {

    private Integer idPlan;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}