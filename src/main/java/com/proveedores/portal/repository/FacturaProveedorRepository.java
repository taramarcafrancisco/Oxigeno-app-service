package com.proveedores.portal.repository;

import com.proveedores.portal.entity.FacturaProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaProveedorRepository extends JpaRepository<FacturaProveedor, Long> {
    List<FacturaProveedor> findByProveedor_IdOrderByFechaEmisionDesc(Long proveedorId);
}
