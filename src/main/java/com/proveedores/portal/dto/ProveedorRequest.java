package com.proveedores.portal.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class ProveedorRequest {
    @NotBlank
    private String razonSocial;

    @NotBlank
    private String cuit;

    @NotBlank
    @Email
    private String email;

    private String telefono;
    private String direccion;

    @NotBlank
    private String rubro;

    private String condicionFiscal;
    private String condicionIva;
    private String documentacionEstado;
    private Boolean activo;

    public String getCondicionFiscal() {
        return condicionFiscal != null ? condicionFiscal : condicionIva;
    }
}
