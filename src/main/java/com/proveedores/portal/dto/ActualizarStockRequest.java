package com.proveedores.portal.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ActualizarStockRequest {
    @NotNull
    @Min(0)
    private Integer stock;
}
