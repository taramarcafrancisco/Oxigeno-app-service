package com.oxigeno.portal.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oxigeno.portal.entity.Rol;
import com.oxigeno.portal.entity.Usuario;
import com.oxigeno.portal.repository.RolRepository;
import com.oxigeno.portal.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional
    public Usuario crear(Usuario usuario) {

        usuario.setEstado(1);

        // En el modelo nuevo NO se asignan planes acá.
        // Los planes se asignan desde UsuarioPlanService / UsuarioPlanController.

        if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
            Integer rolId = usuario.getRoles().iterator().next().getIdRol();

            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            Set<Rol> roles = new HashSet<>();
            roles.add(rol);
            usuario.setRoles(roles);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getRoles() != null) {
            usuario.getRoles().clear();
        }

        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizar(Integer id, Usuario data) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(data.getNombre());
        usuario.setApellido(data.getApellido());
        usuario.setEmail(data.getEmail());
        usuario.setCuit(data.getCuit());
        usuario.setRazonSocial(data.getRazonSocial());
        usuario.setDireccion(data.getDireccion());
        usuario.setTel(data.getTel());

        if (data.getPassword() != null && !data.getPassword().trim().isEmpty()) {
            usuario.setPassword(data.getPassword());
        }

        if (data.getEstado() != null) {
            usuario.setEstado(data.getEstado());
        }

        // ================= ROL =================
        if (data.getRoles() != null && !data.getRoles().isEmpty()) {
            Integer rolId = data.getRoles().iterator().next().getIdRol();

            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            Set<Rol> roles = new HashSet<>();
            roles.add(rol);

            usuario.setRoles(roles);
        }

        // En el modelo nuevo NO se actualizan planes desde Usuario.
        // Eso se hace mediante UsuarioPlanService.

        return usuarioRepository.save(usuario);
    }

 

 
    @Override
    public void cambiarEstado(Integer id, Integer estado) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int nuevoEstado = usuario.getEstado() != null && usuario.getEstado() == 1 ? 0 : 1;

        usuario.setEstado(nuevoEstado);

        usuarioRepository.save(usuario);
    }
 
}