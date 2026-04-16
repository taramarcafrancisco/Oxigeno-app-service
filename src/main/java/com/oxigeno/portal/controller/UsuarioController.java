	package com.oxigeno.portal.controller;
	
	import org.springframework.web.bind.annotation.*;
	
	import com.oxigeno.portal.entity.Usuario;
	import com.oxigeno.portal.entity.UsuarioPlan;
	import com.oxigeno.portal.services.UsuarioPlanService;
	import com.oxigeno.portal.services.UsuarioService;
	
	import java.util.List;
	
		@RestController
		@RequestMapping("/api/usuarios")
		public class UsuarioController {
	
		private final UsuarioService usuarioService;
		private final UsuarioPlanService usuarioPlanService;
	
		public UsuarioController(UsuarioService usuarioService, UsuarioPlanService usuarioPlanService) {
			this.usuarioService = usuarioService;
			this.usuarioPlanService = usuarioPlanService;
		}
	
		@GetMapping
		public List<Usuario> listar() {
			return usuarioService.listar();
		}
	
		@GetMapping("/{id}")
		public Usuario buscar(@PathVariable Integer id) {
			return usuarioService.buscarPorId(id);
		}
	
		@PostMapping
		public Usuario crear(@RequestBody Usuario usuario) {
			return usuarioService.crear(usuario);
		}
	
		@PutMapping("/{id}")
		public Usuario actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
			return usuarioService.actualizar(id, usuario);
		}
	
		@DeleteMapping("/{id}")
		public void eliminar(@PathVariable Integer id) {
			usuarioService.eliminar(id);
		}
	
		@PatchMapping("/{id}/estado")
		public void cambiarEstado(@PathVariable Integer id, @RequestParam Integer estado) {
			usuarioService.cambiarEstado(id, estado);
		}
	
 
	
//		@GetMapping("/{id}/planes")
//		public List<UsuarioPlan> obtenerPlanes(@PathVariable Integer id) {
//			return usuarioPlanService.listarPlanes(id);
//		}
	}
