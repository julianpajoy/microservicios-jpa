package com.formacionbdi.microservicios.app.respuestas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.respuestas.clients.ExamenFeignClient;
import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;
import com.formacionbdi.microservicios.app.respuestas.models.repository.RespuestaRepository;
import com.formacionbdi.microservicios.commnos.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.commnos.examenes.models.entity.Pregunta;

@Service
public class RespuestaServiceImpl implements RespuestaService {

	@Autowired
	private RespuestaRepository repositorio;
	
	@Autowired
	private ExamenFeignClient examenCliente;
	
	
	
	@Override
	@Transactional	// org.springframework.transaction.annotation.Transactional;
	public Iterable<Respuesta> guardarTodo(Iterable<Respuesta> respuestas) {

		return repositorio.saveAll(respuestas);
	}


	@Override
	@Transactional(readOnly = true)
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId) {

		//return repositorio.findRespuestaByAlumnoByExamen(alumnoId, examenId);
		
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
				.collect(Collectors.toList());
		return respuestasIns;
	}


	@Override
	@Transactional(readOnly = true)
	public Iterable<Long> findExamenesIdsConRespuestasByAlumno(Long alumnoId) {

		//return repositorio.findExamenesIdsConRespuestasByAlumno(alumnoId);
		return null;
	}

}
