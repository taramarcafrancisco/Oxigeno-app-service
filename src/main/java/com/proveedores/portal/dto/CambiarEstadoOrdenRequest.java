package com.proveedores.portal.dto;

import com.proveedores.portal.entity.EstadoOrdenCompra;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CambiarEstadoOrdenRequest {
    @NotNull
    private EstadoOrdenCompra estado;
}
