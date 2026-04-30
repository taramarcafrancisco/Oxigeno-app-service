package com.proveedores.portal.dto;

import com.proveedores.portal.entity.Producto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private boolean activo;
    private Long proveedorId;
    private String proveedorRazonSocial;
    private Long categoriaId;
    private String categoriaNombre;

    public static ProductoResponse fromEntity(Producto producto) {
        return ProductoResponse.builder()
            .id(producto.getId())
            .nombre(producto.getNombre())
            .descripcion(producto.getDescripcion())
            .precio(producto.getPrecio())
            .stock(producto.getStock())
            .activo(producto.isActivo())
            .proveedorId(producto.getProveedor().getId())
            .proveedorRazonSocial(producto.getProveedor().getRazonSocial())
            .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
            .categoriaNombre(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null)
            .build();
    }
}
