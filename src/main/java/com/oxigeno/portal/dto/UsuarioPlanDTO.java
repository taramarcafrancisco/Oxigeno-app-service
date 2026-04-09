package com.oxigeno.portal.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UsuarioPlanDTO {

    private Integer idPlan;     // 🔥 mismo nombre que frontend
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;
}