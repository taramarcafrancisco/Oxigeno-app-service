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
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal monto;

    private LocalDate fechaVencimiento;

    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPago estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPago tipoPago;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MedioPago medioPago;

    @Column(length = 250)
    private String comprobante;

    @Column(length = 1000)
    private String observaciones;

    private Integer mesCorrespondiente;

    private Integer anioCorrespondiente;

    @Column(nullable = false)
    private LocalDate fechaAlta;

    @Column(nullable = false)
    private boolean activo = true;

    @PrePersist
    void prePersist() {
        if (fechaAlta == null) {
            fechaAlta = LocalDate.now();
        }
        if (estado == null) {
            estado = EstadoPago.PENDIENTE;
        }
        if (tipoPago == null) {
            tipoPago = TipoPago.GENERICO;
        }
    }
}
