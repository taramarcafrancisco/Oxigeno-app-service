package com.proveedores.portal.services;

import com.proveedores.portal.dto.ProveedorRequest;
import com.proveedores.portal.entity.Proveedor;
import com.proveedores.portal.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional
    public Proveedor crearProveedor(ProveedorRequest request) {
        validarProveedorNuevo(request);

        Proveedor proveedor = Proveedor.builder()
            .razonSocial(request.getRazonSocial())
            .cuit(request.getCuit())
            .email(request.getEmail())
            .telefono(request.getTelefono())
            .direccion(request.getDireccion())
            .rubro(request.getRubro())
            .condicionFiscal(request.getCondicionFiscal())
            .documentacionEstado(valorODefault(request.getDocumentacionEstado(), "VIGENTE"))
            .fechaAlta(LocalDate.now())
            .activo(true)
            .build();
        System.out.println("[ProveedorService] Guardando proveedor: " + proveedor.getRazonSocial());
        return proveedorRepository.save(proveedor);
    }

    @Transactional
    public Proveedor actualizarProveedor(Long id, ProveedorRequest request) {
        Proveedor proveedor = obtenerProveedor(id);
        validarProveedorExistente(id, request);

        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setCuit(request.getCuit());
        proveedor.setEmail(request.getEmail());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setRubro(request.getRubro());
        proveedor.setCondicionFiscal(request.getCondicionFiscal());
        proveedor.setDocumentacionEstado(valorODefault(request.getDocumentacionEstado(), proveedor.getDocumentacionEstado()));
        proveedor.setActivo(request.getActivo() != null ? request.getActivo() : proveedor.isActivo());
        return proveedorRepository.save(proveedor);
    }

    @Transactional
    public void desactivarProveedor(Long id) {
        Proveedor proveedor = obtenerProveedor(id);
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    @Transactional(readOnly = true)
    public List<Proveedor> listarActivos() {
        return proveedorRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Proveedor obtenerProveedor(Long id) {
        return proveedorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + id));
    }

    private void validarProveedorNuevo(ProveedorRequest request) {
        validarCamposObligatorios(request);
        if (proveedorRepository.existsByCuit(request.getCuit())) {
            throw new IllegalArgumentException("Ya existe un proveedor con CUIT: " + request.getCuit());
        }
        if (proveedorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un proveedor con email: " + request.getEmail());
        }
    }

    private void validarProveedorExistente(Long id, ProveedorRequest request) {
        validarCamposObligatorios(request);
        if (proveedorRepository.existsByCuitAndIdNot(request.getCuit(), id)) {
            throw new IllegalArgumentException("Ya existe un proveedor con CUIT: " + request.getCuit());
        }
        if (proveedorRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Ya existe un proveedor con email: " + request.getEmail());
        }
    }

    private void validarCamposObligatorios(ProveedorRequest request) {
        if (request.getCondicionFiscal() == null || request.getCondicionFiscal().trim().isEmpty()) {
            throw new IllegalArgumentException("La condicion fiscal es obligatoria");
        }
    }

    private String valorODefault(String valor, String defaultValue) {
        return valor == null || valor.trim().isEmpty() ? defaultValue : valor;
    }
}
