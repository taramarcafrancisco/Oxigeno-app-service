package com.proveedores.portal.controller;

import com.proveedores.portal.dto.CambiarEstadoOrdenRequest;
import com.proveedores.portal.dto.CrearOrdenCompraRequest;
import com.proveedores.portal.dto.OrdenCompraItemRequest;
import com.proveedores.portal.dto.OrdenCompraResponse;
import com.proveedores.portal.services.OrdenCompraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    @GetMapping
    public List<OrdenCompraResponse> listar() {
        return ordenCompraService.listar().stream()
            .map(OrdenCompraResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<OrdenCompraResponse> crear(@Valid @RequestBody CrearOrdenCompraRequest request) {
        return ResponseEntity.ok(OrdenCompraResponse.fromEntity(ordenCompraService.crearOrden(request)));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<OrdenCompraResponse> agregarItem(@PathVariable Long id,
                                                           @Valid @RequestBody OrdenCompraItemRequest request) {
        return ResponseEntity.ok(OrdenCompraResponse.fromEntity(ordenCompraService.agregarItem(id, request)));
    }

    @PutMapping("/{id}/calcular-total")
    public ResponseEntity<OrdenCompraResponse> calcularTotal(@PathVariable Long id) {
        return ResponseEntity.ok(OrdenCompraResponse.fromEntity(ordenCompraService.calcularTotal(id)));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenCompraResponse> cambiarEstado(@PathVariable Long id,
                                                             @Valid @RequestBody CambiarEstadoOrdenRequest request) {
        return ResponseEntity.ok(OrdenCompraResponse.fromEntity(ordenCompraService.cambiarEstado(id, request.getEstado())));
    }
}
