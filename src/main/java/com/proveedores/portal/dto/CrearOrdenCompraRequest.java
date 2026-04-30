package com.proveedores.portal.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CrearOrdenCompraRequest {
    @NotNull(message = "El proveedorId es obligatorio")
    private Long proveedorId;

    @Valid
    @NotEmpty(message = "Debe agregar al menos un item")
    private List<OrdenCompraItemRequest> items = new ArrayList<>();
}
