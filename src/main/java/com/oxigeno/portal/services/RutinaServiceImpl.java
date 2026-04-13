package com.oxigeno.portal.services;

import com.oxigeno.portal.dto.RutinaEjercicioRequest;
import com.oxigeno.portal.dto.RutinaEjercicioResponse;
import com.oxigeno.portal.dto.RutinaRequest;
import com.oxigeno.portal.dto.RutinaResponse;
import com.oxigeno.portal.entity.Rutina;
import com.oxigeno.portal.entity.RutinaEjercicio;
import com.oxigeno.portal.entity.Usuario;
import com.oxigeno.portal.repository.RutinaEjercicioRepository;
import com.oxigeno.portal.repository.RutinaRepository;
import com.oxigeno.portal.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RutinaServiceImpl implements RutinaService {

	private final RutinaRepository rutinaRepository;
	private final UsuarioRepository usuarioRepository;
	private final RutinaEjercicioRepository rutinaEjercicioRepository;

	public RutinaServiceImpl(RutinaRepository rutinaRepository, UsuarioRepository usuarioRepository,
			RutinaEjercicioRepository rutinaEjercicioRepository) {
		this.rutinaRepository = rutinaRepository;
		this.usuarioRepository = usuarioRepository;
		this.rutinaEjercicioRepository = rutinaEjercicioRepository;
	}

	@Transactional(readOnly = true)
	public Rutina getRutinaCompletaDelUsuario(Integer idUsuario) {
		Rutina rutina = rutinaRepository.findFirstByUsuario_IdUsuarioAndActivaTrueOrderByFechaAsignacionDesc(idUsuario)
				.orElseThrow(() -> new RuntimeException("No se encontró rutina activa"));

		List<RutinaEjercicio> ejercicios = rutinaEjercicioRepository
				.findByRutina_IdRutinaOrderByOrdenAsc(rutina.getIdRutina());

		rutina.setEjercicios(ejercicios);

		return rutina;
	}

	@Override
	@Transactional
	public RutinaResponse crearRutina(Integer usuarioId, RutinaRequest request) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		if (Boolean.TRUE.equals(request.getActiva())) {
			desactivarRutinasActivas(usuarioId);
		}

		Rutina rutina = new Rutina();
		rutina.setUsuario(usuario);
		rutina.setNombre(request.getNombre());
		rutina.setObjetivo(request.getObjetivo());
		rutina.setNivel(request.getNivel());
		rutina.setDias(request.getDias());
		rutina.setObservaciones(request.getObservaciones());
		rutina.setFechaAsignacion(LocalDate.now());
		rutina.setActiva(request.getActiva() == null ? Boolean.TRUE : request.getActiva());

		if (rutina.getEjercicios() == null) {
			rutina.setEjercicios(new java.util.ArrayList<>());
		}

		if (request.getEjercicios() != null) {
			for (RutinaEjercicioRequest item : request.getEjercicios()) {
				RutinaEjercicio ejercicio = new RutinaEjercicio();
				ejercicio.setRutina(rutina);
				ejercicio.setEjercicio(item.getEjercicio());
				ejercicio.setSeries(item.getSeries());
				ejercicio.setRepeticiones(item.getRepeticiones());
				ejercicio.setDescanso(item.getDescanso());
				ejercicio.setOrden(item.getOrden());
				rutina.getEjercicios().add(ejercicio);
			}
		}

		return toResponse(rutinaRepository.save(rutina));
	}

	@Override
	@Transactional
	public RutinaResponse actualizarRutina(Integer rutinaId, RutinaRequest request) {
		Rutina rutina = rutinaRepository.findById(rutinaId)
				.orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

		if (Boolean.TRUE.equals(request.getActiva())) {
			desactivarRutinasActivas(rutina.getUsuario().getIdUsuario());
		}

		rutina.setNombre(request.getNombre());
		rutina.setObjetivo(request.getObjetivo());
		rutina.setNivel(request.getNivel());
		rutina.setDias(request.getDias());
		rutina.setObservaciones(request.getObservaciones());
		rutina.setActiva(request.getActiva() == null ? rutina.getActiva() : request.getActiva());

		if (rutina.getEjercicios() == null) {
			rutina.setEjercicios(new java.util.ArrayList<>());
		} else {
			rutina.getEjercicios().clear();
		}

		if (request.getEjercicios() != null) {
			for (RutinaEjercicioRequest item : request.getEjercicios()) {
				RutinaEjercicio ejercicio = new RutinaEjercicio();
				ejercicio.setRutina(rutina);
				ejercicio.setEjercicio(item.getEjercicio());
				ejercicio.setSeries(item.getSeries());
				ejercicio.setRepeticiones(item.getRepeticiones());
				ejercicio.setDescanso(item.getDescanso());
				ejercicio.setOrden(item.getOrden());
				rutina.getEjercicios().add(ejercicio);
			}
		}

		return toResponse(rutinaRepository.save(rutina));
	}

	@Override
	@Transactional(readOnly = true)
	public List<RutinaResponse> listarPorUsuario(Integer usuarioId) {
		return rutinaRepository.findByUsuario_IdUsuarioOrderByFechaAsignacionDesc(usuarioId).stream()
				.map(this::toResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public RutinaResponse obtenerRutinaActivaPorUsuario(Integer usuarioId) {
		return rutinaRepository.findFirstByUsuario_IdUsuarioAndActivaTrueOrderByFechaAsignacionDesc(usuarioId)
				.map(rutina -> {
					List<RutinaEjercicio> ejercicios = rutinaEjercicioRepository
							.findByRutina_IdRutinaOrderByOrdenAsc(rutina.getIdRutina());
					rutina.setEjercicios(ejercicios);
					return toResponse(rutina);
				}).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public RutinaResponse obtenerRutinaActivaPorEmail(String email) {
		return rutinaRepository.findFirstByUsuario_EmailAndActivaTrueOrderByFechaAsignacionDesc(email).map(rutina -> {
			List<RutinaEjercicio> ejercicios = rutinaEjercicioRepository
					.findByRutina_IdRutinaOrderByOrdenAsc(rutina.getIdRutina());
			rutina.setEjercicios(ejercicios);
			return toResponse(rutina);
		}).orElse(null);
	}

	private void desactivarRutinasActivas(Integer usuarioId) {
		List<Rutina> rutinas = rutinaRepository.findByUsuario_IdUsuarioOrderByFechaAsignacionDesc(usuarioId);
		for (Rutina r : rutinas) {
			if (Boolean.TRUE.equals(r.getActiva())) {
				r.setActiva(Boolean.FALSE);
			}
		}
		rutinaRepository.saveAll(rutinas);
	}

	private RutinaResponse toResponse(Rutina rutina) {
		return RutinaResponse.builder().idRutina(rutina.getIdRutina()).nombre(rutina.getNombre())
				.objetivo(rutina.getObjetivo()).nivel(rutina.getNivel()).dias(rutina.getDias())
				.observaciones(rutina.getObservaciones()).fechaAsignacion(rutina.getFechaAsignacion())
				.activa(rutina.getActiva())
				.idUsuario(rutina.getUsuario() != null ? rutina.getUsuario().getIdUsuario() : null)
				.ejercicios(rutina.getEjercicios() == null ? Collections.emptyList()
						: rutina.getEjercicios().stream()
								.map(e -> RutinaEjercicioResponse.builder().idRutinaEjercicio(e.getIdRutinaEjercicio())
										.ejercicio(e.getEjercicio()).series(e.getSeries())
										.repeticiones(e.getRepeticiones()).descanso(e.getDescanso()).orden(e.getOrden())
										.build())
								.collect(Collectors.toList()))
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public RutinaResponse obtenerRutinaCompletaPorEmail(String email) {
		Rutina rutina = rutinaRepository.findFirstByUsuario_EmailAndActivaTrueOrderByFechaAsignacionDesc(email)
				.orElseThrow(() -> new RuntimeException("No se encontró rutina activa"));

		List<RutinaEjercicio> ejercicios = rutinaEjercicioRepository
				.findByRutina_IdRutinaOrderByOrdenAsc(rutina.getIdRutina());

		rutina.setEjercicios(ejercicios);

		return toResponse(rutina);
	}
}