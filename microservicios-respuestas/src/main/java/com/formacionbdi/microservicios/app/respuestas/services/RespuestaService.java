package com.formacionbdi.microservicios.app.respuestas.services;

import com.formacionbdi.microservicios.app.respuestas.models.entity.Respuesta;

public interface RespuestaService {
	
	/*
	 * Cuando el alumno responde las preguntas, lo que hace es enviar un monton
	 * de preguntas que estan relacionadas con el examen; por lo tanto, el controlador
	 * va a recibir una lista de respuestas, todas relacionadas con el examen.
	 * 
	 * Por tanto, este service y en el controlador va a guardar varias 
	 * respuestas «guardarTodo»
	 */
	public Iterable<Respuesta> guardarTodo(Iterable<Respuesta> respuestas);
		
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);
	
	public Iterable<Long> findExamenesIdsConRespuestasByAlumno(Long alumnoId);
	
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId);

}
