package com.proveedores.portal.dto;

import com.proveedores.portal.entity.EstadoFacturaProveedor;
import com.proveedores.portal.entity.MedioPago;
import com.proveedores.portal.entity.TipoComprobanteFactura;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FacturaProveedorRequest {
    @NotBlank(message = "El numero de factura es obligatorio")
    private String numeroFactura;

    private String puntoVenta;

    @NotNull(message = "El tipo de comprobante es obligatorio")
    private TipoComprobanteFactura tipoComprobante;

    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private String condicionIVAProveedor;
    private String cuitProveedor;
    private String razonSocialProveedor;
    private String concepto;
    private String descripcion;

    @DecimalMin(value = "0.0")
    private BigDecimal importeNeto;

    @DecimalMin(value = "0.0")
    private BigDecimal iva;

    @DecimalMin(value = "0.0")
    private BigDecimal otrosImpuestos;

    @DecimalMin(value = "0.0")
    private BigDecimal percepciones;

    @DecimalMin(value = "0.0")
    private BigDecimal retenciones;

    @NotNull(message = "El importe total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El importe total debe ser mayor a cero")
    private BigDecimal importeTotal;

    private String moneda;
    private EstadoFacturaProveedor estado;
    private MedioPago medioPago;
    private String comprobantePago;
    private String observaciones;
    private String rutaArchivo;
    private String usuarioCarga;
}
