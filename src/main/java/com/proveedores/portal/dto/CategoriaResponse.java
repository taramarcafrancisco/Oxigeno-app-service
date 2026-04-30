package com.proveedores.portal.dto;

import com.proveedores.portal.entity.Categoria;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String descripcion;

    public static CategoriaResponse fromEntity(Categoria categoria) {
        return CategoriaResponse.builder()
            .id(categoria.getId())
            .nombre(categoria.getNombre())
            .descripcion(categoria.getDescripcion())
            .build();
    }
}
