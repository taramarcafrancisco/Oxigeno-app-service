package com.proveedores.portal.dto;

import com.proveedores.portal.entity.Proveedor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ProveedorResponse {
    private Long id;
    private String razonSocial;
    private String cuit;
    private String email;
    private String telefono;
    private String direccion;
    private String rubro;
    private String condicionFiscal;
    private String condicionIva;
    private String documentacionEstado;
    private String estado;
    private boolean activo;
    private LocalDate fechaAlta;

    public static ProveedorResponse fromEntity(Proveedor proveedor) {
        return ProveedorResponse.builder()
            .id(proveedor.getId())
            .razonSocial(proveedor.getRazonSocial())
            .cuit(proveedor.getCuit())
            .email(proveedor.getEmail())
            .telefono(proveedor.getTelefono())
            .direccion(proveedor.getDireccion())
            .rubro(proveedor.getRubro())
            .condicionFiscal(proveedor.getCondicionFiscal())
            .condicionIva(proveedor.getCondicionFiscal())
            .documentacionEstado(proveedor.getDocumentacionEstado())
            .estado(proveedor.isActivo() ? "ACTIVO" : "INACTIVO")
            .activo(proveedor.isActivo())
            .fechaAlta(proveedor.getFechaAlta())
            .build();
    }
}
