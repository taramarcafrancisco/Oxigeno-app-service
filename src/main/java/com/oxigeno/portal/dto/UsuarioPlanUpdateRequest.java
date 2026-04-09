package com.oxigeno.portal.dto;
 

import java.time.LocalDate;
import lombok.Data;

@Data
public class UsuarioPlanUpdateRequest {
    private Integer idPlan;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
	public Boolean Activo ;
		
}
