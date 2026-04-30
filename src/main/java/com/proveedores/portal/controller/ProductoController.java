package com.proveedores.portal.controller;

import com.proveedores.portal.dto.ActualizarStockRequest;
import com.proveedores.portal.dto.ProductoRequest;
import com.proveedores.portal.dto.ProductoResponse;
import com.proveedores.portal.services.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponse> listar(@RequestParam(required = false) Long proveedorId) {
        return (proveedorId == null
            ? productoService.listarActivos()
            : productoService.buscarPorProveedor(proveedorId))
            .stream()
            .map(ProductoResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(ProductoResponse.fromEntity(productoService.altaProducto(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(ProductoResponse.fromEntity(productoService.actualizarProducto(id, request)));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductoResponse> actualizarStock(@PathVariable Long id,
                                                            @Valid @RequestBody ActualizarStockRequest request) {
        return ResponseEntity.ok(ProductoResponse.fromEntity(productoService.actualizarStock(id, request.getStock())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
