package com.proveedores.portal.services;

import com.proveedores.portal.dto.CategoriaRequest;
import com.proveedores.portal.entity.Categoria;
import com.proveedores.portal.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @Transactional
    public Categoria crear(CategoriaRequest request) {
        return categoriaRepository.save(Categoria.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .build());
    }

    @Transactional
    public Categoria actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = obtener(id);
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Categoria obtener(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada: " + id));
    }
}
