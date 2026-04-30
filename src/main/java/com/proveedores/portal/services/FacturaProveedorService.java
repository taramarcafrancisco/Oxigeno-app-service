package com.proveedores.portal.services;

import com.proveedores.portal.dto.FacturaProveedorRequest;
import com.proveedores.portal.entity.EstadoFacturaProveedor;
import com.proveedores.portal.entity.FacturaProveedor;
import com.proveedores.portal.entity.Proveedor;
import com.proveedores.portal.repository.FacturaProveedorRepository;
import com.proveedores.portal.repository.ProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaProveedorService {

    private final FacturaProveedorRepository facturaProveedorRepository;
    private final ProveedorRepository proveedorRepository;

    public FacturaProveedorService(FacturaProveedorRepository facturaProveedorRepository,
                                   ProveedorRepository proveedorRepository) {
        this.facturaProveedorRepository = facturaProveedorRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<FacturaProveedor> listarPorProveedor(Long proveedorId) {
        validarProveedorExistente(proveedorId);
        List<FacturaProveedor> facturas = facturaProveedorRepository.findByProveedor_IdOrderByFechaEmisionDesc(proveedorId);
        facturas.forEach(this::actualizarEstadoVencidoEnMemoria);
        return facturas;
    }

    @Transactional
    public FacturaProveedor crearFactura(Long proveedorId, FacturaProveedorRequest request) {
        Proveedor proveedor = obtenerProveedor(proveedorId);

        FacturaProveedor factura = FacturaProveedor.builder()
            .proveedor(proveedor)
            .numeroFactura(request.getNumeroFactura())
            .puntoVenta(request.getPuntoVenta())
            .tipoComprobante(request.getTipoComprobante())
            .fechaEmision(request.getFechaEmision())
            .fechaVencimiento(request.getFechaVencimiento())
            .fechaPago(request.getFechaPago())
            .condicionIVAProveedor(valorODefault(request.getCondicionIVAProveedor(), proveedor.getCondicionFiscal()))
            .cuitProveedor(valorODefault(request.getCuitProveedor(), proveedor.getCuit()))
            .razonSocialProveedor(valorODefault(request.getRazonSocialProveedor(), proveedor.getRazonSocial()))
            .concepto(request.getConcepto())
            .descripcion(request.getDescripcion())
            .importeNeto(request.getImporteNeto())
            .iva(request.getIva())
            .otrosImpuestos(request.getOtrosImpuestos())
            .percepciones(request.getPercepciones())
            .retenciones(request.getRetenciones())
            .importeTotal(request.getImporteTotal())
            .moneda(valorODefault(request.getMoneda(), "ARS"))
            .estado(request.getEstado())
            .medioPago(request.getMedioPago())
            .comprobantePago(request.getComprobantePago())
            .observaciones(request.getObservaciones())
            .rutaArchivo(request.getRutaArchivo())
            .fechaCarga(LocalDate.now())
            .usuarioCarga(request.getUsuarioCarga())
            .build();

        normalizarEstado(factura);
        return facturaProveedorRepository.save(factura);
    }

    @Transactional(readOnly = true)
    public FacturaProveedor obtenerFactura(Long id) {
        FacturaProveedor factura = facturaProveedorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada: " + id));
        actualizarEstadoVencidoEnMemoria(factura);
        return factura;
    }

    @Transactional
    public FacturaProveedor actualizarFactura(Long id, FacturaProveedorRequest request) {
        FacturaProveedor factura = facturaProveedorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada: " + id));

        factura.setNumeroFactura(request.getNumeroFactura());
        factura.setPuntoVenta(request.getPuntoVenta());
        factura.setTipoComprobante(request.getTipoComprobante());
        factura.setFechaEmision(request.getFechaEmision());
        factura.setFechaVencimiento(request.getFechaVencimiento());
        factura.setFechaPago(request.getFechaPago());
        factura.setCondicionIVAProveedor(request.getCondicionIVAProveedor());
        factura.setCuitProveedor(request.getCuitProveedor());
        factura.setRazonSocialProveedor(request.getRazonSocialProveedor());
        factura.setConcepto(request.getConcepto());
        factura.setDescripcion(request.getDescripcion());
        factura.setImporteNeto(request.getImporteNeto());
        factura.setIva(request.getIva());
        factura.setOtrosImpuestos(request.getOtrosImpuestos());
        factura.setPercepciones(request.getPercepciones());
        factura.setRetenciones(request.getRetenciones());
        factura.setImporteTotal(request.getImporteTotal());
        factura.setMoneda(valorODefault(request.getMoneda(), "ARS"));
        factura.setEstado(request.getEstado());
        factura.setMedioPago(request.getMedioPago());
        factura.setComprobantePago(request.getComprobantePago());
        factura.setObservaciones(request.getObservaciones());
        factura.setRutaArchivo(request.getRutaArchivo());
        factura.setUsuarioCarga(request.getUsuarioCarga());

        normalizarEstado(factura);
        return facturaProveedorRepository.save(factura);
    }

    @Transactional
    public void eliminarFactura(Long id) {
        if (!facturaProveedorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Factura no encontrada: " + id);
        }
        facturaProveedorRepository.deleteById(id);
    }

    private void validarProveedorExistente(Long proveedorId) {
        if (!proveedorRepository.existsById(proveedorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado: " + proveedorId);
        }
    }

    private Proveedor obtenerProveedor(Long proveedorId) {
        return proveedorRepository.findById(proveedorId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado: " + proveedorId));
    }

    private void normalizarEstado(FacturaProveedor factura) {
        if (factura.getEstado() == null) {
            factura.setEstado(EstadoFacturaProveedor.PENDIENTE);
        }
        if (EstadoFacturaProveedor.PAGADA.equals(factura.getEstado()) && factura.getFechaPago() == null) {
            factura.setFechaPago(LocalDate.now());
        }
        actualizarEstadoVencidoEnMemoria(factura);
    }

    private void actualizarEstadoVencidoEnMemoria(FacturaProveedor factura) {
        if (factura.getFechaVencimiento() == null || EstadoFacturaProveedor.PAGADA.equals(factura.getEstado())
            || EstadoFacturaProveedor.ANULADA.equals(factura.getEstado())) {
            return;
        }
        if (factura.getFechaVencimiento().isBefore(LocalDate.now())) {
            factura.setEstado(EstadoFacturaProveedor.VENCIDA);
        }
    }

    private String valorODefault(String valor, String defaultValue) {
        return valor == null || valor.trim().isEmpty() ? defaultValue : valor;
    }
}
