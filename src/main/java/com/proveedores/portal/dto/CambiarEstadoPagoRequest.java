package com.proveedores.portal.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CambiarEstadoPagoRequest {
    @NotBlank
    private String estado;
}
