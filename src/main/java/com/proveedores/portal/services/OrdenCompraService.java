package com.proveedores.portal.services;

import com.proveedores.portal.dto.CrearOrdenCompraRequest;
import com.proveedores.portal.dto.OrdenCompraItemRequest;
import com.proveedores.portal.entity.EstadoOrdenCompra;
import com.proveedores.portal.entity.OrdenCompra;
import com.proveedores.portal.entity.OrdenCompraItem;
import com.proveedores.portal.entity.Producto;
import com.proveedores.portal.entity.Proveedor;
import com.proveedores.portal.repository.OrdenCompraRepository;
import com.proveedores.portal.repository.ProductoRepository;
import com.proveedores.portal.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;

    public OrdenCompraService(OrdenCompraRepository ordenCompraRepository,
                              ProveedorRepository proveedorRepository,
                              ProductoRepository productoRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public OrdenCompra crearOrden(CrearOrdenCompraRequest request) {
        validarCrearOrdenRequest(request);

        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
            .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + request.getProveedorId()));

        OrdenCompra orden = OrdenCompra.builder()
            .proveedor(proveedor)
            .fecha(LocalDate.now())
            .estado(EstadoOrdenCompra.PENDIENTE)
            .total(BigDecimal.ZERO)
            .listaItems(new ArrayList<>())
            .build();

        for (OrdenCompraItemRequest itemRequest : request.getItems()) {
            agregarItemInterno(orden, itemRequest);
        }

        calcularTotal(orden);
        return ordenCompraRepository.save(orden);
    }

    @Transactional
    public OrdenCompra agregarItem(Long ordenId, OrdenCompraItemRequest request) {
        OrdenCompra orden = obtener(ordenId);
        validarOrdenEditable(orden);
        agregarItemInterno(orden, request);
        calcularTotal(orden);
        return ordenCompraRepository.save(orden);
    }

    @Transactional
    public OrdenCompra calcularTotal(Long ordenId) {
        OrdenCompra orden = obtener(ordenId);
        calcularTotal(orden);
        return ordenCompraRepository.save(orden);
    }

    @Transactional
    public OrdenCompra cambiarEstado(Long ordenId, EstadoOrdenCompra estado) {
        OrdenCompra orden = obtener(ordenId);
        orden.setEstado(estado);
        return ordenCompraRepository.save(orden);
    }

    @Transactional(readOnly = true)
    public List<OrdenCompra> listar() {
        return ordenCompraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public OrdenCompra obtener(Long id) {
        return ordenCompraRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Orden de compra no encontrada: " + id));
    }

    private void agregarItemInterno(OrdenCompra orden, OrdenCompraItemRequest request) {
        validarItemRequest(request);
        asegurarListaItems(orden);

        Producto producto = productoRepository.findById(request.getProductoId())
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + request.getProductoId()));

        if (!producto.getProveedor().getId().equals(orden.getProveedor().getId())) {
            throw new IllegalArgumentException("El producto no pertenece al proveedor de la orden");
        }

        BigDecimal precioUnitario = request.getPrecioUnitario() != null
            ? request.getPrecioUnitario()
            : producto.getPrecio();

        OrdenCompraItem item = OrdenCompraItem.builder()
            .ordenCompra(orden)
            .producto(producto)
            .cantidad(request.getCantidad())
            .precioUnitario(precioUnitario)
            .build();
        orden.getListaItems().add(item);
    }

    private void calcularTotal(OrdenCompra orden) {
        asegurarListaItems(orden);
        BigDecimal total = orden.getListaItems().stream()
            .map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        orden.setTotal(total);
    }

    private void validarCrearOrdenRequest(CrearOrdenCompraRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La orden de compra es obligatoria");
        }
        if (request.getProveedorId() == null) {
            throw new IllegalArgumentException("El proveedorId es obligatorio");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos un item");
        }
        for (OrdenCompraItemRequest itemRequest : request.getItems()) {
            validarItemRequest(itemRequest);
        }
    }

    private void validarItemRequest(OrdenCompraItemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El item de la orden no puede ser null");
        }
        if (request.getProductoId() == null) {
            throw new IllegalArgumentException("El productoId es obligatorio");
        }
        if (request.getCantidad() == null) {
            throw new IllegalArgumentException("La cantidad es obligatoria");
        }
        if (request.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (request.getPrecioUnitario() != null && request.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precioUnitario debe ser mayor a cero");
        }
    }

    private void asegurarListaItems(OrdenCompra orden) {
        if (orden.getListaItems() == null) {
            orden.setListaItems(new ArrayList<>());
        }
    }

    private void validarOrdenEditable(OrdenCompra orden) {
        if (orden.getEstado() != EstadoOrdenCompra.PENDIENTE) {
            throw new IllegalArgumentException("Solo se pueden modificar ordenes pendientes");
        }
    }
}
