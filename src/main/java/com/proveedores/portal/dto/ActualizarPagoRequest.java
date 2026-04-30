package com.proveedores.portal.dto;

import com.proveedores.portal.entity.EstadoPago;
import com.proveedores.portal.entity.MedioPago;
import com.proveedores.portal.entity.TipoPago;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ActualizarPagoRequest {
    private String descripcion;
    private Long proveedorId;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal monto;

    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private EstadoPago estado;
    private TipoPago tipoPago;
    private MedioPago medioPago;
    private String comprobante;
    private String observaciones;
    private Integer mesCorrespondiente;
    private Integer anioCorrespondiente;
}
