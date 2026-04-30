package com.proveedores.portal.dto;

import com.proveedores.portal.entity.MedioPago;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MarcarPagoRequest {
    private LocalDate fechaPago;
    private MedioPago medioPago;
    private String comprobante;
    private String observaciones;
}
