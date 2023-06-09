package com.formacionbdi.microservicios.app.cursos.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.cursos.clients.AlumnoFeignClient;
import com.formacionbdi.microservicios.app.cursos.clients.RespuestaFeignClient;
import com.formacionbdi.microservicios.app.cursos.models.enity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.repository.CursoRepository;
import com.formacionbdi.microservicios.commnos.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.comun.services.ComunServiceImpl;

/*Para que se registre como un componente Spring
y despues lo podamos inyectar "Autowired"*/
@Service
public class CursoServiceImpl extends ComunServiceImpl<Curso, CursoRepository> implements CursoService {

	
	/*
	 * Inyeccion de la interfaz HTTP Client Fiegn de listado de examenes
	 */
	@Autowired
	private RespuestaFeignClient cliente;	// (1 Feign)
	
	@Autowired
	private AlumnoFeignClient clienteAlumno;	// (2 Feign)

	/*
	 * @Autowired
	 * 
	 * protected CursoRepository repositorioCrud;
	 * 
	 * @Override
	 * @Transactional(readOnly = true) 
	 * public Iterable<Curso> buscarTodo() {	<-- I CursoService: public Iterable<Curso> buscarTodo();
	 * 		return repositorioCrud.findAll();	<-- CRUD: Iterable<T> findAll();
	 * }
	 * 
	 * 
	 * @Override
	 * @Transactional(readOnly = true)
	 * public Optional<Curso> buscarById(Long id) {
	 * 		return repositorioCrud.findById(id); 
	 * }
	 * 
	 * 
	 * @Override
	 * @Transactional
	 * public Curso guardar(Curso entity) {
	 * 		return repositorioCrud.save(entity); 
	 * }
	 * 
	 * 
	 * @Override
	 * @Transactional
	 * public void deleteById(Long id) {
	 * 		repositorioCrud.deleteById(id);
	 * }
	 */
	
	
	@Override
	@Transactional(readOnly=true)
	public Curso findCursoByAlumnoId(Long id) {
		
		/*
		 *	 @Query("select crs from Curso crs join fetch crs.alumnos alm where alm.id=?1")
		 *	 public Curso findCursoByAlumnoId(Long id);
		 */
		
		return repositorioCrud.findCursoByAlumnoIdRepository(id);
	}

	
	@Override
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long alumnoId) { // (1 Feign)

		return cliente.obtenerExamenesIdsConRespuestasAlumno(alumnoId);
	}


	@Override
	public Iterable<Alumno> obtenerAlumnosPorCurso(Iterable<Long> ids) {

		return clienteAlumno.obtenerAlumnosPorCurso(ids);
	}


	@Override
	@Transactional
	public void eliminarCursoAlumnoPorId(Long id) {
		
		repositorioCrud.eliminarCursoAlumnoPorId(id);	
	}

}
