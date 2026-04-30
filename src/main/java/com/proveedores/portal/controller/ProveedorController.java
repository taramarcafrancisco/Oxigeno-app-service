package com.proveedores.portal.controller;

import com.proveedores.portal.dto.ProveedorRequest;
import com.proveedores.portal.dto.ProveedorResponse;
import com.proveedores.portal.services.ProveedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<ProveedorResponse> listar() {
        return proveedorService.listarActivos().stream()
            .map(ProveedorResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProveedorResponse> crear(@Valid @RequestBody ProveedorRequest request) {
        System.out.println("[ProveedorController] POST /api/proveedores recibido: " + request);
        return ResponseEntity.ok(ProveedorResponse.fromEntity(proveedorService.crearProveedor(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ProveedorResponse.fromEntity(proveedorService.obtenerProveedor(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponse> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(ProveedorResponse.fromEntity(proveedorService.actualizarProveedor(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proveedorService.desactivarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}
