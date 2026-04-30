package com.proveedores.portal.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductoRequest {
    @NotBlank
    private String nombre;
    private String descripcion;

    @NotNull
    private BigDecimal precio;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    private Long proveedorId;

    private Long categoriaId;
}
