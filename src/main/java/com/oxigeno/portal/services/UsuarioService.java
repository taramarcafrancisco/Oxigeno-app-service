		package com.oxigeno.portal.services;
		
		import java.util.List;
		
		 
		import com.oxigeno.portal.entity.Usuario;
		
		public interface UsuarioService {
		    Usuario crear(Usuario usuario);
		    List<Usuario> listar();
		    Usuario buscarPorId(Integer id);
		    void eliminar(Integer id);
			Usuario actualizar(Integer id, Usuario usuario);
	 
		 
			void cambiarEstado(Integer id, Integer estado);

		}