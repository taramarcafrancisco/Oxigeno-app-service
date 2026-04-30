package com.proveedores.portal.repository;

import com.proveedores.portal.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByProveedorId(Long proveedorId);
}
