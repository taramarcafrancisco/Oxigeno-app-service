package com.proveedores.portal.repository;

import com.proveedores.portal.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByActivoTrue();
    boolean existsByCuit(String cuit);
    boolean existsByEmail(String email);
    boolean existsByCuitAndIdNot(String cuit, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
