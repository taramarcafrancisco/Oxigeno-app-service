package com.proveedores.portal.dto;

import com.proveedores.portal.entity.EstadoFacturaProveedor;
import com.proveedores.portal.entity.FacturaProveedor;
import com.proveedores.portal.entity.MedioPago;
import com.proveedores.portal.entity.TipoComprobanteFactura;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class FacturaProveedorResponse {
    private Long id;
    private Long proveedorId;
    private String numeroFactura;
    private String puntoVenta;
    private TipoComprobanteFactura tipoComprobante;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private String condicionIVAProveedor;
    private String cuitProveedor;
    private String razonSocialProveedor;
    private String concepto;
    private String descripcion;
    private BigDecimal importeNeto;
    private BigDecimal iva;
    private BigDecimal otrosImpuestos;
    private BigDecimal percepciones;
    private BigDecimal retenciones;
    private BigDecimal importeTotal;
    private String moneda;
    private EstadoFacturaProveedor estado;
    private MedioPago medioPago;
    private String comprobantePago;
    private String observaciones;
    private String rutaArchivo;
    private LocalDate fechaCarga;
    private String usuarioCarga;

    public static FacturaProveedorResponse fromEntity(FacturaProveedor factura) {
        return FacturaProveedorResponse.builder()
            .id(factura.getId())
            .proveedorId(factura.getProveedor() != null ? factura.getProveedor().getId() : null)
            .numeroFactura(factura.getNumeroFactura())
            .puntoVenta(factura.getPuntoVenta())
            .tipoComprobante(factura.getTipoComprobante())
            .fechaEmision(factura.getFechaEmision())
            .fechaVencimiento(factura.getFechaVencimiento())
            .fechaPago(factura.getFechaPago())
            .condicionIVAProveedor(factura.getCondicionIVAProveedor())
            .cuitProveedor(factura.getCuitProveedor())
            .razonSocialProveedor(factura.getRazonSocialProveedor())
            .concepto(factura.getConcepto())
            .descripcion(factura.getDescripcion())
            .importeNeto(factura.getImporteNeto())
            .iva(factura.getIva())
            .otrosImpuestos(factura.getOtrosImpuestos())
            .percepciones(factura.getPercepciones())
            .retenciones(factura.getRetenciones())
            .importeTotal(factura.getImporteTotal())
            .moneda(factura.getMoneda())
            .estado(factura.getEstado())
            .medioPago(factura.getMedioPago())
            .comprobantePago(factura.getComprobantePago())
            .observaciones(factura.getObservaciones())
            .rutaArchivo(factura.getRutaArchivo())
            .fechaCarga(factura.getFechaCarga())
            .usuarioCarga(factura.getUsuarioCarga())
            .build();
    }
}
