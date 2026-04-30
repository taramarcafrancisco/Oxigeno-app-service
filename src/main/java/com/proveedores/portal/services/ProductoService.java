package com.proveedores.portal.services;

import com.proveedores.portal.dto.ProductoRequest;
import com.proveedores.portal.entity.Categoria;
import com.proveedores.portal.entity.Producto;
import com.proveedores.portal.entity.Proveedor;
import com.proveedores.portal.repository.CategoriaRepository;
import com.proveedores.portal.repository.ProductoRepository;
import com.proveedores.portal.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository,
                           ProveedorRepository proveedorRepository,
                           CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Producto altaProducto(ProductoRequest request) {
        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
            .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + request.getProveedorId()));
        Categoria categoria = obtenerCategoriaOpcional(request.getCategoriaId());
        Producto producto = Producto.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .precio(request.getPrecio())
            .stock(request.getStock())
            .proveedor(proveedor)
            .categoria(categoria)
            .activo(true)
            .build();
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(Long id, ProductoRequest request) {
        Producto producto = obtener(id);
        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
            .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + request.getProveedorId()));
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setProveedor(proveedor);
        producto.setCategoria(obtenerCategoriaOpcional(request.getCategoriaId()));
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarStock(Long id, Integer stock) {
        Producto producto = obtener(id);
        producto.setStock(stock);
        return productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorProveedor(Long proveedorId) {
        return productoRepository.findByProveedorId(proveedorId);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Producto obtener(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
    }

    @Transactional
    public void desactivar(Long id) {
        Producto producto = obtener(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    private Categoria obtenerCategoriaOpcional(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada: " + categoriaId));
    }
}
