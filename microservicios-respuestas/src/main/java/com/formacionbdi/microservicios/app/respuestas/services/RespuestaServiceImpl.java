package com.formacionbdi.microservicios.app.respuestas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.app.respuestas.models.repository.RespuestaRepository;

@Service
public class RespuestaServiceImpl implements RespuestaService {

	@Autowired
	private RespuestaRepository repositorio;
	
	/*@Autowired
	private ExamenFeignClient examenCliente;*/
	
	
	
	@Override
	public Iterable<Respuesta> guardarTodo(Iterable<Respuesta> respuestas) {

		return repositorio.saveAll(respuestas);
	}


	@Override
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId) {

		/*
		 * Este codigo es para consultar a traves de la bbdd distribuida usando Feign
		 * 
		 * return repositorio.findRespuestaByAlumnoByExamen(alumnoId, examenId);
		
		Examen examenIns = examenCliente.obtenerExamenPorId(examenId); // Cliente Feign; le pasa "examenId"
		
		List<Pregunta> preguntasIns = examenIns.getPreguntas(); // Obtenemos la preguntas a través del "examenIns"
		
		// Se convierte la lista de "preguntasIns" a lista con los IDs de preguntas
		List<Long> preguntaIdsIns = preguntasIns
				.stream() // convertir flujo de datos "Pregunta" a otro tipo dato "Long"
				.map(p -> p.getId()) // obtenemos las preguntas luego obtienen los IDs que son tipo "Long"
				.collect(Collectors.toList()); // se pasa lo convertido en stream a un List con datos tipo Long
		
		List<Respuesta> respuestasIns = (List<Respuesta>) repositorio.findRespuestaByAlumnoByPreguntaIds(alumnoId, preguntaIdsIns);
		
		// Ahora se asocia respuesta con su objeto pregunta
		respuestasIns = respuestasIns
				.stream()
				.map(r -> { // r Respuesta
					preguntasIns.forEach(p -> {	// Por cada p Pregunta del Examen
						if(p.getId() == r.getPreguntaId()) {
							r.setPregunta(p);	// Si son iguales se asigna la p Pregunta a la r Respuesta
						}
					});
					return r;	// Retorna la r Respuesta modificada con el objeto Pregunta
				})
				.collect(Collectors.toList());*/
		List<Respuesta> respuestasIns = (List<Respuesta>) repositorio.findRespuestaByAlumnoByExamen(alumnoId, examenId);
		
		return respuestasIns;
	}


	@Override
	public Iterable<Long> findExamenesIdsConRespuestasByAlumno(Long alumnoId) {

		/* 
		 * Este codigo es para consultar a traves de la bbdd distribuida usando Feign
		 * 
		 * Obtener todas las respuestas desde MongoDB buscada por el Id del Alumno
		 *
		List<Respuesta> respuestasAlumno = (List<Respuesta>) repositorio.findByAlumnoId(alumnoId);
		
		// Se crea una lista vacia con los Ids de los examenes class "Collections"
		List<Long> examenIds = Collections.emptyList();
		
		// Validar que al menos haya un respuesta del Alumno por Id
		if(respuestasAlumno.size() > 0) {
			
			// Convertir la lista "respuestasAlumno" para obtener lista de preguntas-Id
			List<Long> preguntaIds = respuestasAlumno
					.stream()
					.map(r -> r.getPreguntaId())
					.collect(Collectors.toList());
			
			/*	Usando Feign se llama al endpoint para obtener los examenes que corresponden 
			 	a las "preguntaIds" //
			examenIds = examenCliente.obtenerExamenesIdsPorPreguntasIdRespondidas(preguntaIds);
		}
		
		/* return repositorio.findExamenesIdsConRespuestasByAlumno(alumnoId); */
		
		List<Respuesta> respuestasAlumno = (List<Respuesta>) repositorio.findExamenesIdsConRespuestasByAlumno(alumnoId);
		List<Long> examenIds = respuestasAlumno
				.stream()
				.map(r -> r.getPregunta().getExamen().getId()) // Obtenemos los Ids del examen de tipo Long
				.distinct()	// Los ids del examen se repite por cada pregunta (3veces) deja un solo valor.
				.collect(Collectors.toList());
		
		return examenIds;
	}


	@Override
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId) {

		return repositorio.findByAlumnoId(alumnoId);
	}

}
