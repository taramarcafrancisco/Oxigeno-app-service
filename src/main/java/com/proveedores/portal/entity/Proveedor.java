package com.proveedores.portal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String razonSocial;

    @Column(nullable = false, unique = true, length = 20)
    private String cuit;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(length = 60)
    private String telefono;

    @Column(length = 250)
    private String direccion;

    @Column(length = 120)
    private String rubro;

    @Column(length = 80)
    private String condicionFiscal;

    @Column(length = 40)
    private String documentacionEstado;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private LocalDate fechaAlta;

    @PrePersist
    void prePersist() {
        if (fechaAlta == null) {
            fechaAlta = LocalDate.now();
        }
        if (documentacionEstado == null || documentacionEstado.trim().isEmpty()) {
            documentacionEstado = "VIGENTE";
        }
    }
}
