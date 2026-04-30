package com.proveedores.portal.services;

import com.proveedores.portal.dto.ActualizarPagoRequest;
import com.proveedores.portal.dto.CrearPagoRequest;
import com.proveedores.portal.dto.MarcarPagoRequest;
import com.proveedores.portal.entity.EstadoPago;
import com.proveedores.portal.entity.Pago;
import com.proveedores.portal.entity.Proveedor;
import com.proveedores.portal.entity.TipoPago;
import com.proveedores.portal.repository.PagoRepository;
import com.proveedores.portal.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagoService {

    private static final int ANIO_MINIMO = 1900;
    private static final int ANIO_MAXIMO = 2100;

    private final PagoRepository pagoRepository;
    private final ProveedorRepository proveedorRepository;

    public PagoService(PagoRepository pagoRepository, ProveedorRepository proveedorRepository) {
        this.pagoRepository = pagoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional
    public Pago crearPago(CrearPagoRequest request) {
        validarDescripcion(request.getDescripcion());
        validarMonto(request.getMonto());
        validarMesAnio(request.getMesCorrespondiente(), request.getAnioCorrespondiente(), request.getTipoPago());

        Proveedor proveedor = obtenerProveedorOpcional(request.getProveedorId());
        validarDuplicadoUnicoMensual(null, request.getDescripcion(), proveedor, request.getMesCorrespondiente(),
            request.getAnioCorrespondiente(), request.getTipoPago());

        Pago pago = Pago.builder()
            .descripcion(request.getDescripcion().trim())
            .proveedor(proveedor)
            .monto(request.getMonto())
            .fechaVencimiento(request.getFechaVencimiento())
            .fechaPago(request.getFechaPago())
            .estado(request.getEstado())
            .tipoPago(request.getTipoPago())
            .medioPago(request.getMedioPago())
            .comprobante(request.getComprobante())
            .observaciones(request.getObservaciones())
            .mesCorrespondiente(request.getMesCorrespondiente())
            .anioCorrespondiente(request.getAnioCorrespondiente())
            .activo(true)
            .build();

        actualizarEstadoSegunReglas(pago);
        return pagoRepository.save(pago);
    }

    @Transactional
    public Pago actualizarPago(Long id, ActualizarPagoRequest request) {
        Pago pago = obtenerPago(id);
        EstadoPago nuevoEstado = request.getEstado();

        if (request.getDescripcion() != null) {
            validarDescripcion(request.getDescripcion());
            pago.setDescripcion(request.getDescripcion().trim());
        }
        if (request.getProveedorId() != null) {
            pago.setProveedor(obtenerProveedorOpcional(request.getProveedorId()));
        }
        if (request.getMonto() != null) {
            validarMonto(request.getMonto());
            pago.setMonto(request.getMonto());
        }
        if (request.getFechaVencimiento() != null) {
            pago.setFechaVencimiento(request.getFechaVencimiento());
        }
        if (request.getFechaPago() != null) {
            pago.setFechaPago(request.getFechaPago());
        }
        if (request.getTipoPago() != null) {
            pago.setTipoPago(request.getTipoPago());
        }
        if (request.getMedioPago() != null) {
            pago.setMedioPago(request.getMedioPago());
        }
        if (request.getComprobante() != null) {
            pago.setComprobante(request.getComprobante());
        }
        if (request.getObservaciones() != null) {
            pago.setObservaciones(request.getObservaciones());
        }
        if (request.getMesCorrespondiente() != null) {
            pago.setMesCorrespondiente(request.getMesCorrespondiente());
        }
        if (request.getAnioCorrespondiente() != null) {
            pago.setAnioCorrespondiente(request.getAnioCorrespondiente());
        }

        validarMonto(pago.getMonto());
        validarMesAnio(pago.getMesCorrespondiente(), pago.getAnioCorrespondiente(), pago.getTipoPago());
        validarDuplicadoUnicoMensual(pago.getId(), pago.getDescripcion(), pago.getProveedor(),
            pago.getMesCorrespondiente(), pago.getAnioCorrespondiente(), pago.getTipoPago());
        if (nuevoEstado != null && nuevoEstado != pago.getEstado()) {
            aplicarCambioEstado(pago, nuevoEstado);
        } else {
            actualizarEstadoSegunReglas(pago);
        }
        return pagoRepository.save(pago);
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPagos() {
        return pagoRepository.buscarPorFiltros(null, null, null, null, null);
    }

    @Transactional(readOnly = true)
    public Pago obtenerPago(Long id) {
        Pago pago = pagoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + id));
        if (!pago.isActivo()) {
            throw new IllegalArgumentException("Pago no encontrado: " + id);
        }
        return pago;
    }

    @Transactional
    public Pago marcarComoPagado(Long id, MarcarPagoRequest request) {
        Pago pago = obtenerPago(id);
        validarCambioEstado(pago, EstadoPago.PAGADO);

        LocalDate fechaPago = request != null ? request.getFechaPago() : null;
        pago.setFechaPago(fechaPago != null ? fechaPago : LocalDate.now());
        if (request != null && request.getMedioPago() != null) {
            pago.setMedioPago(request.getMedioPago());
        }
        if (request != null && request.getComprobante() != null) {
            pago.setComprobante(request.getComprobante());
        }
        if (request != null && request.getObservaciones() != null) {
            pago.setObservaciones(request.getObservaciones());
        }
        pago.setEstado(EstadoPago.PAGADO);
        return pagoRepository.save(pago);
    }

    @Transactional
    public Pago marcarComoPagado(Long id) {
        return marcarComoPagado(id, null);
    }

    @Transactional
    public Pago marcarComoVencido(Long id) {
        return cambiarEstado(id, EstadoPago.VENCIDO);
    }

    @Transactional
    public Pago cancelarPago(Long id) {
        return cambiarEstado(id, EstadoPago.CANCELADO);
    }

    @Transactional
    public Pago cambiarEstado(Long id, EstadoPago nuevoEstado) {
        Pago pago = obtenerPago(id);
        aplicarCambioEstado(pago, nuevoEstado);
        return pagoRepository.save(pago);
    }

    @Transactional
    public void eliminarLogico(Long id) {
        Pago pago = obtenerPago(id);
        pago.setActivo(false);
        pagoRepository.save(pago);
    }

    @Transactional
    public int actualizarEstadosVencidos() {
        List<Pago> pagos = pagoRepository.findByEstadoAndFechaVencimientoBeforeAndActivoTrue(EstadoPago.PENDIENTE, LocalDate.now());
        int actualizados = 0;
        for (Pago pago : pagos) {
            pago.setEstado(EstadoPago.VENCIDO);
            actualizados++;
            pagoRepository.save(pago);
        }
        return actualizados;
    }

    @Transactional(readOnly = true)
    public List<Pago> listarPorFiltros(EstadoPago estado, TipoPago tipoPago, Long proveedorId, Integer mes, Integer anio) {
        validarMesAnioFiltro(mes, anio);
        return pagoRepository.buscarPorFiltros(estado, tipoPago, proveedorId, mes, anio);
    }

    @Transactional
    public List<Pago> generarPagosMensuales(Integer mes, Integer anio) {
        validarMes(mes);
        validarAnio(anio);

        List<Pago> pagosBase = pagoRepository.findByTipoPagoAndActivoTrue(TipoPago.MENSUAL);
        List<Pago> generados = new ArrayList<Pago>();

        for (Pago pagoBase : pagosBase) {
            Long proveedorId = pagoBase.getProveedor() != null ? pagoBase.getProveedor().getId() : null;
            boolean existe = pagoRepository.existsByDescripcionIgnoreCaseAndProveedorIdAndMesCorrespondienteAndAnioCorrespondienteAndTipoPago(
                pagoBase.getDescripcion(), proveedorId, mes, anio, TipoPago.MENSUAL);

            if (!existe) {
                Pago nuevoPago = Pago.builder()
                    .descripcion(pagoBase.getDescripcion())
                    .proveedor(pagoBase.getProveedor())
                    .monto(pagoBase.getMonto())
                    .fechaVencimiento(calcularVencimientoMensual(pagoBase.getFechaVencimiento(), mes, anio))
                    .estado(EstadoPago.PENDIENTE)
                    .tipoPago(TipoPago.MENSUAL)
                    .medioPago(pagoBase.getMedioPago())
                    .observaciones(pagoBase.getObservaciones())
                    .mesCorrespondiente(mes)
                    .anioCorrespondiente(anio)
                    .activo(true)
                    .build();
                actualizarEstadoSegunReglas(nuevoPago);
                generados.add(pagoRepository.save(nuevoPago));
            }
        }

        return generados;
    }

    private Proveedor obtenerProveedorOpcional(Long proveedorId) {
        if (proveedorId == null) {
            return null;
        }
        return proveedorRepository.findById(proveedorId)
            .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + proveedorId));
    }

    private void actualizarEstadoSegunReglas(Pago pago) {
        if (pago.getEstado() == EstadoPago.CANCELADO) {
            return;
        }
        if (pago.getEstado() == EstadoPago.PAGADO) {
            if (pago.getFechaPago() == null) {
                pago.setFechaPago(LocalDate.now());
            }
            return;
        }
        if (pago.getFechaPago() != null) {
            pago.setEstado(EstadoPago.PAGADO);
            return;
        }
        if (pago.getFechaVencimiento() != null && pago.getFechaVencimiento().isBefore(LocalDate.now())) {
            pago.setEstado(EstadoPago.VENCIDO);
            return;
        }
        if (pago.getEstado() == null || pago.getEstado() == EstadoPago.VENCIDO) {
            pago.setEstado(EstadoPago.PENDIENTE);
        }
    }

    private void aplicarCambioEstado(Pago pago, EstadoPago nuevoEstado) {
        validarCambioEstado(pago, nuevoEstado);

        if (nuevoEstado == EstadoPago.PAGADO && pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDate.now());
        }
        pago.setEstado(nuevoEstado);
    }

    private void validarCambioEstado(Pago pago, EstadoPago nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }
        if (!pago.isActivo()) {
            throw new IllegalArgumentException("No se puede cambiar el estado de un pago inactivo");
        }
        if (pago.getEstado() == EstadoPago.CANCELADO) {
            throw new IllegalArgumentException("Un pago cancelado no puede volver a modificarse");
        }
        if (pago.getEstado() == nuevoEstado) {
            return;
        }
        if (pago.getEstado() == EstadoPago.PAGADO && nuevoEstado != EstadoPago.CANCELADO) {
            throw new IllegalArgumentException("Un pago pagado no puede volver a pendiente ni marcarse como vencido");
        }
        if (pago.getEstado() == EstadoPago.PENDIENTE &&
            (nuevoEstado == EstadoPago.PAGADO || nuevoEstado == EstadoPago.VENCIDO || nuevoEstado == EstadoPago.CANCELADO)) {
            return;
        }
        if (pago.getEstado() == EstadoPago.VENCIDO &&
            (nuevoEstado == EstadoPago.PAGADO || nuevoEstado == EstadoPago.CANCELADO)) {
            return;
        }

        throw new IllegalArgumentException("Cambio de estado no permitido: " + pago.getEstado() + " a " + nuevoEstado);
    }

    private LocalDate calcularVencimientoMensual(LocalDate fechaBase, Integer mes, Integer anio) {
        if (fechaBase == null) {
            return null;
        }
        int dia = Math.min(fechaBase.getDayOfMonth(), LocalDate.of(anio, mes, 1).lengthOfMonth());
        return LocalDate.of(anio, mes, dia);
    }

    private void validarDuplicadoUnicoMensual(Long id, String descripcion, Proveedor proveedor, Integer mes,
                                              Integer anio, TipoPago tipoPago) {
        if (tipoPago != TipoPago.UNICO_MENSUAL) {
            return;
        }

        Long proveedorId = proveedor != null ? proveedor.getId() : null;
        boolean duplicado = id == null
            ? pagoRepository.existsByDescripcionIgnoreCaseAndProveedorIdAndMesCorrespondienteAndAnioCorrespondienteAndTipoPago(
                descripcion, proveedorId, mes, anio, TipoPago.UNICO_MENSUAL)
            : pagoRepository.existsDuplicadoExcluyendoId(id, descripcion, proveedorId, mes, anio, TipoPago.UNICO_MENSUAL);

        if (duplicado) {
            throw new IllegalArgumentException("Ya existe un pago unico mensual para esa descripcion, proveedor, mes y anio");
        }
    }

    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion es obligatoria");
        }
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
    }

    private void validarMesAnio(Integer mes, Integer anio, TipoPago tipoPago) {
        if (tipoPago == null) {
            throw new IllegalArgumentException("El tipo de pago es obligatorio");
        }
        if (mes != null) {
            validarMes(mes);
        }
        if (anio != null) {
            validarAnio(anio);
        }
        if (tipoPago == TipoPago.UNICO_MENSUAL && (mes == null || anio == null)) {
            throw new IllegalArgumentException("Los pagos unico mensual requieren mes y anio correspondiente");
        }
    }

    private void validarMesAnioFiltro(Integer mes, Integer anio) {
        if (mes != null) {
            validarMes(mes);
        }
        if (anio != null) {
            validarAnio(anio);
        }
    }

    private void validarMes(Integer mes) {
        if (mes == null || mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes correspondiente debe estar entre 1 y 12");
        }
    }

    private void validarAnio(Integer anio) {
        if (anio == null || anio < ANIO_MINIMO || anio > ANIO_MAXIMO) {
            throw new IllegalArgumentException("El anio correspondiente no es valido");
        }
    }
}
