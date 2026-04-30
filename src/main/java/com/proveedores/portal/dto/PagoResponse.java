package com.proveedores.portal.dto;

import com.proveedores.portal.entity.EstadoPago;
import com.proveedores.portal.entity.MedioPago;
import com.proveedores.portal.entity.Pago;
import com.proveedores.portal.entity.TipoPago;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class PagoResponse {
    private Long id;
    private String descripcion;
    private Long proveedorId;
    private String proveedorRazonSocial;
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
    private LocalDate fechaAlta;
    private boolean activo;

    public static PagoResponse fromEntity(Pago pago) {
        return PagoResponse.builder()
            .id(pago.getId())
            .descripcion(pago.getDescripcion())
            .proveedorId(pago.getProveedor() != null ? pago.getProveedor().getId() : null)
            .proveedorRazonSocial(pago.getProveedor() != null ? pago.getProveedor().getRazonSocial() : null)
            .monto(pago.getMonto())
            .fechaVencimiento(pago.getFechaVencimiento())
            .fechaPago(pago.getFechaPago())
            .estado(pago.getEstado())
            .tipoPago(pago.getTipoPago())
            .medioPago(pago.getMedioPago())
            .comprobante(pago.getComprobante())
            .observaciones(pago.getObservaciones())
            .mesCorrespondiente(pago.getMesCorrespondiente())
            .anioCorrespondiente(pago.getAnioCorrespondiente())
            .fechaAlta(pago.getFechaAlta())
            .activo(pago.isActivo())
            .build();
    }
}
