package com.formacionbdi.microservicios.app.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-cursos")
public interface CursoFeignClient {
	
	@DeleteMapping("/eliminar-alumno-del-curso/{id}")
	public void eliminarAlumnoPorCursoId(@PathVariable Long id);

}
