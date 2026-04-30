package com.proveedores.portal.controller;

import com.proveedores.portal.dto.FacturaProveedorRequest;
import com.proveedores.portal.dto.FacturaProveedorResponse;
import com.proveedores.portal.services.FacturaProveedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proveedores/{proveedorId}/facturas")
@CrossOrigin(origins = "http://localhost:5173")
public class FacturaProveedorController {

    private final FacturaProveedorService facturaProveedorService;

    public FacturaProveedorController(FacturaProveedorService facturaProveedorService) {
        this.facturaProveedorService = facturaProveedorService;
    }

    @GetMapping
    public List<FacturaProveedorResponse> listarPorProveedor(@PathVariable Long proveedorId) {
        return facturaProveedorService.listarPorProveedor(proveedorId).stream()
            .map(FacturaProveedorResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<FacturaProveedorResponse> crear(@PathVariable Long proveedorId,
                                                          @Valid @RequestBody FacturaProveedorRequest request) {
        return ResponseEntity.ok(FacturaProveedorResponse.fromEntity(
            facturaProveedorService.crearFactura(proveedorId, request)
        ));
    }
}
