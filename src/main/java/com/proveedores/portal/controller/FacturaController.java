package com.proveedores.portal.controller;

import com.proveedores.portal.dto.FacturaProveedorRequest;
import com.proveedores.portal.dto.FacturaProveedorResponse;
import com.proveedores.portal.services.FacturaProveedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "http://localhost:5173")
public class FacturaController {

    private final FacturaProveedorService facturaProveedorService;

    public FacturaController(FacturaProveedorService facturaProveedorService) {
        this.facturaProveedorService = facturaProveedorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaProveedorResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(FacturaProveedorResponse.fromEntity(facturaProveedorService.obtenerFactura(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacturaProveedorResponse> actualizar(@PathVariable Long id,
                                                               @Valid @RequestBody FacturaProveedorRequest request) {
        return ResponseEntity.ok(FacturaProveedorResponse.fromEntity(
            facturaProveedorService.actualizarFactura(id, request)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        facturaProveedorService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
}
