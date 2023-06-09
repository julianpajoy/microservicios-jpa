package com.formacionbdi.microservicios.app.usuarios.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
 
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.usuarios.client.CursoFeignClient;
//import com.formacionbdi.microservicios.app.usuarios.models.repository.AlumnoRepository;
import com.formacionbdi.microservicios.app.usuarios.models.repository.AlumnoRepository;
import com.formacionbdi.microservicios.commnos.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.comun.services.ComunServiceImpl;



@Service
// public class AlumnoServiceImpl implements AlumnoServiceContrato {
public class AlumnoServiceImpl extends ComunServiceImpl<Alumno, AlumnoRepository> implements AlumnoServiceContrato {

	@Autowired
	private CursoFeignClient clienteCurso;
	
	@Override
	@Transactional(readOnly=true) // como es un select es por eso readOnly
	public List<Alumno> findByNombreOrApellido(String termino) {

		return repositorioCrud.findByNombreOrApellido(termino);
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<Alumno> buscarTodoPorId(Iterable<Long> ids) {
		
		return repositorioCrud.findAllById(ids);
	}

	/*
	 * Este es un cliente Feign por tanto el @Transactional no va.
	 */
	@Override
	public void eliminarAlumnoPorCursoId(Long id) {
		
		clienteCurso.eliminarAlumnoPorCursoId(id);
	}

	/*
	 * Se implementa en "deleteById" porque se hace uso del @Transactional
	 * dado de que si falla la comunicacion por alguna razon, no se va
	 * eliminar ni de MariaDB (Curso), ni de Postgres(Alumno). Con esto se asegura la integridad
	 * de los datos.
	 */
	@Override
	@Transactional
	public void deleteById(Long id) {
		super.deleteById(id);
		this.eliminarAlumnoPorCursoId(id);
	}

	/*
	 * Se sobreescriben los metodos buscarTodo y buscarById
	 * para que sean ordenados ascendente por Id.
	 * Por bbdd Postgre
	 */
	@Override
	@Transactional(readOnly=true)
	public Iterable<Alumno> buscarTodo() {

		return repositorioCrud.findAllByOrderByIdAsc();
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Alumno> buscarTodo(Pageable paginable) {
		
		return repositorioCrud.findAllByOrderByIdAsc(paginable);
	}

	

	
	/*
	 * @Autowired private AlumnoRepository repositorioCrud;
	 * 
	 * 
	 * Los metodos de consulta se anotan con "@Transactional" de lectura, por
	 * ejemplo para Select, con "(readOnly = true)".
	 * 
	 * 
	 * @Override
	 * @Transactional(readOnly = true)
	 * public Iterable<Alumno> buscarTodo() { <-- I AlumnoServiceContrato: public Iterable<Curso> buscarTodo();
	 * 		return repositorioCrud.findAll(); <-- CRUD: Iterable<T> findAll();
	 * }
	 * 
	 * 
	 * @Override
	 * @Transactional(readOnly = true)
	 * public Optional<Alumno> findById(Long id) {
	 * 		return repositorioCrud.findById(id);
	 * }
	 * 
	 * 
	 * @Override
	 * @Transactional
	 * public Alumno guardar(Alumno alumno) {
	 * 		return repositorioCrud.save(alumno);
	 * }
	 * 
	 * 
	 * @Override
	 * @Transactional
	 * public void deleteById(Long id) {
	 * 		repositorioCrud.deleteById(id);
	 * }
	 */
}
