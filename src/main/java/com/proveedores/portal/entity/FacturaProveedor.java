package com.proveedores.portal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "facturas_proveedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(nullable = false, length = 80)
    private String numeroFactura;

    @Column(length = 20)
    private String puntoVenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoComprobanteFactura tipoComprobante;

    private LocalDate fechaEmision;

    private LocalDate fechaVencimiento;

    private LocalDate fechaPago;

    @Column(length = 80)
    private String condicionIVAProveedor;

    @Column(length = 20)
    private String cuitProveedor;

    @Column(length = 180)
    private String razonSocialProveedor;

    @Column(length = 180)
    private String concepto;

    @Column(length = 1000)
    private String descripcion;

    @Column(precision = 14, scale = 2)
    private BigDecimal importeNeto;

    @Column(precision = 14, scale = 2)
    private BigDecimal iva;

    @Column(precision = 14, scale = 2)
    private BigDecimal otrosImpuestos;

    @Column(precision = 14, scale = 2)
    private BigDecimal percepciones;

    @Column(precision = 14, scale = 2)
    private BigDecimal retenciones;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal importeTotal;

    @Column(nullable = false, length = 10)
    private String moneda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoFacturaProveedor estado;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MedioPago medioPago;

    @Column(length = 250)
    private String comprobantePago;

    @Column(length = 1000)
    private String observaciones;

    @Column(length = 500)
    private String rutaArchivo;

    @Column(nullable = false)
    private LocalDate fechaCarga;

    @Column(length = 120)
    private String usuarioCarga;

    @PrePersist
    void prePersist() {
        if (fechaCarga == null) {
            fechaCarga = LocalDate.now();
        }
        if (estado == null) {
            estado = EstadoFacturaProveedor.PENDIENTE;
        }
        if (moneda == null || moneda.trim().isEmpty()) {
            moneda = "ARS";
        }
    }
}
