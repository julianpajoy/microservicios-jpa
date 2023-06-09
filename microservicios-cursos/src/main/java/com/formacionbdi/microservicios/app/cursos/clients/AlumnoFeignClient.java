package com.formacionbdi.microservicios.app.cursos.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.formacionbdi.microservicios.commnos.alumnos.models.entity.Alumno;

@FeignClient(name = "microservicio-usuarios") // Properties - microservicio Usuarios
public interface AlumnoFeignClient {
	
	/*
	 * Sigue el metodo(Endpoint) que vamos a consumir
	 * del microservicio Usuarios
	 */
	@GetMapping("/alumnos-por-curso")
	public Iterable<Alumno> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
}
