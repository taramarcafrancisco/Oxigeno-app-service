package com.proveedores.portal.controller;

import com.proveedores.portal.dto.ActualizarPagoRequest;
import com.proveedores.portal.dto.CambiarEstadoPagoRequest;
import com.proveedores.portal.dto.CrearPagoRequest;
import com.proveedores.portal.dto.MarcarPagoRequest;
import com.proveedores.portal.dto.PagoResponse;
import com.proveedores.portal.entity.EstadoPago;
import com.proveedores.portal.entity.TipoPago;
import com.proveedores.portal.services.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/pagos")
@PreAuthorize("hasRole('ADMIN')")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> crear(@Valid @RequestBody CrearPagoRequest request) {
        return ResponseEntity.ok(PagoResponse.fromEntity(pagoService.crearPago(request)));
    }

    @GetMapping
    public List<PagoResponse> listar() {
        return pagoService.listarPagos().stream()
            .map(PagoResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(PagoResponse.fromEntity(pagoService.obtenerPago(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponse> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody ActualizarPagoRequest request) {
        return ResponseEntity.ok(PagoResponse.fromEntity(pagoService.actualizarPago(id, request)));
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<PagoResponse> marcarComoPagado(@PathVariable Long id,
                                                         @RequestBody(required = false) MarcarPagoRequest request) {
        return ResponseEntity.ok(PagoResponse.fromEntity(pagoService.marcarComoPagado(id, request)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoResponse> cambiarEstado(@PathVariable Long id,
                                                      @Valid @RequestBody CambiarEstadoPagoRequest request) {
        EstadoPago estado = EstadoPago.valueOf(request.getEstado().trim().toUpperCase());
        return ResponseEntity.ok(PagoResponse.fromEntity(pagoService.cambiarEstado(id, estado)));
    }

    @PatchMapping("/{id}/vencer")
    public ResponseEntity<PagoResponse> marcarComoVencido(@PathVariable Long id) {
        return ResponseEntity.ok(PagoResponse.fromEntity(pagoService.marcarComoVencido(id)));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PagoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(PagoResponse.fromEntity(pagoService.cancelarPago(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtros")
    public List<PagoResponse> listarPorFiltros(@RequestParam(required = false) EstadoPago estado,
                                               @RequestParam(required = false) TipoPago tipoPago,
                                               @RequestParam(required = false) Long proveedorId,
                                               @RequestParam(required = false) Integer mes,
                                               @RequestParam(required = false) Integer anio) {
        return pagoService.listarPorFiltros(estado, tipoPago, proveedorId, mes, anio).stream()
            .map(PagoResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @PostMapping("/generar-mensuales")
    public List<PagoResponse> generarMensuales(@RequestParam Integer mes,
                                               @RequestParam Integer anio) {
        return pagoService.generarPagosMensuales(mes, anio).stream()
            .map(PagoResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @PostMapping("/actualizar-vencidos")
    public ResponseEntity<Integer> actualizarEstadosVencidos() {
        return ResponseEntity.ok(pagoService.actualizarEstadosVencidos());
    }
}
