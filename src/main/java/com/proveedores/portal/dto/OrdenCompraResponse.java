package com.proveedores.portal.dto;

import com.proveedores.portal.entity.EstadoOrdenCompra;
import com.proveedores.portal.entity.OrdenCompra;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrdenCompraResponse {
    private Long id;
    private Long proveedorId;
    private String proveedorRazonSocial;
    private LocalDate fecha;
    private BigDecimal total;
    private EstadoOrdenCompra estado;
    private List<Item> listaItems;

    public static OrdenCompraResponse fromEntity(OrdenCompra orden) {
        return OrdenCompraResponse.builder()
            .id(orden.getId())
            .proveedorId(orden.getProveedor().getId())
            .proveedorRazonSocial(orden.getProveedor().getRazonSocial())
            .fecha(orden.getFecha())
            .total(orden.getTotal())
            .estado(orden.getEstado())
            .listaItems(orden.getListaItems().stream().map(item -> Item.builder()
                .id(item.getId())
                .productoId(item.getProducto().getId())
                .productoNombre(item.getProducto().getNombre())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                .build()).collect(Collectors.toList()))
            .build();
    }

    @Getter
    @Builder
    public static class Item {
        private Long id;
        private Long productoId;
        private String productoNombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
