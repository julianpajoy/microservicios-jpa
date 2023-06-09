package com.formacionbdi.microservicios.app.cursos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.cursos.models.enity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.enity.CursoAlumno;
import com.formacionbdi.microservicios.app.cursos.services.CursoService;
import com.formacionbdi.microservicios.commnos.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commnos.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.comun.controllers.CommonController;

import jakarta.validation.Valid;



@RestController
public class CursoController extends CommonController<Curso, CursoService>{
	
	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
/*
 *	public class ComunController<ENT, SRV extends ComunContrato<ENT>> {
 * 
 *	protected CursoService servicio;
 * 
 * 	@GetMapping("/")
 *	public ResponseEntity<?> listar(){
 *  	return ResponseEntity.ok().body(servicio.buscarTodo( ));	<-- CursoService (ComunContrato)
 *  }
 *  
 *  public ResponseEntity<?> ver(@PathVariable(name = "id") Long idEntity){
 *  	Optional<Curso> idEncontrado = servicio.findById(idEntity);
 *  		if(idEncontrado.isEmpty()) {	
 *  			return ResponseEntity.notFound().build();
 *  		}
 *  		else{			
 *  			return ResponseEntity.ok(idEncontrado);
 *  		}
 *  }
 *  
 *  @PostMapping("/")
 *  public ResponseEntity <?> crear(@RequestBody ENT entity){
 *  	Curso entityDb = servicio.guardar(entity);		<-- CursoService
 *  		return ResponseEntity.status(HttpStatus.CREATED).body(entityDb);
 *  }
 */
	

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult resultado, @PathVariable Long id){
		
		// Ahora se valida antes de editar usando el metodo "validar"
		if(resultado.hasErrors()) {
			return this.validar(resultado);
		}
		
		Optional<Curso> validacion = this.servicio.buscarById(id);
		
		if (!validacion.isPresent()) {
			return ResponseEntity.notFound().build();
		}

			Curso dbCurso = validacion.get();
			dbCurso.setNombre(curso.getNombre());
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(this.servicio.guardar(dbCurso));

	}
	
	/*
	 * @PostMapping("/{id}")
	 * @ResponseStatus(HttpStatus.CREATED)
	 * public Curso editar(@RequestBody Curso curso, @PathVariable Long id){ 
	 * 	Optional<Curso> validacion = this.servicio.findById(id); if (!validacion.isPresent()) {
	 * 		throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	 * 	}
	 * 		Curso dbCurso = validacion.get();
	 * 		dbCurso.setNombre(curso.getNombre());
	 * 		return this.servicio.guardar(dbCurso);
	 * }
	 */

	
	/*
	 * Un metodo para ensayar el balanceador de carga Load Balancer.
	 */
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		
		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("Balanceador: ", balanceadorTest);
		respuesta.put("Listado de cursos: ", servicio.buscarTodo());
		return ResponseEntity.ok(respuesta);
	}

	
	@PutMapping("/{id}/asignar-alumnos") // nombre de ruta
	public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id){
		
		Optional<Curso> validacion = this.servicio.buscarById(id);
		if (!validacion.isPresent()) {
			return ResponseEntity.notFound().build();
		}

			Curso dbCurso = validacion.get();
			
		// lambda foreach
			alumnos.forEach(elemento -> {
				
					CursoAlumno cursoAlumnoIns = new CursoAlumno();
					cursoAlumnoIns.setAlumnoId(elemento.getId());
					cursoAlumnoIns.setCurso(dbCurso);
					
					// dbCurso.addAlumno(elemento);
					dbCurso.addCursoAlumno(cursoAlumnoIns);
			});
			
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(this.servicio.guardar(dbCurso));
	}
	
	
/* 
 * Es importante que el objeto alumno que vamos a eliminar contenga el ID; en la clase
 * Entity Alumno, implementamos el equals para comprar 
 */
	@PutMapping("/{id}/eliminar-alumno") 
	public ResponseEntity<?> eliminarAlumno(@RequestBody Alumno alumnoR, @PathVariable Long id){
		
		Optional<Curso> validacion = this.servicio.buscarById(id);
		if (!validacion.isPresent()) {
			return ResponseEntity.notFound().build();
		}

			Curso dbCurso = validacion.get();
			
			CursoAlumno cursoAlumnoIns = new CursoAlumno();
			cursoAlumnoIns.setAlumnoId(alumnoR.getId());
			// dbCurso.removeAlumno(alumnoR);
			dbCurso.removeCursoAlumno(cursoAlumnoIns);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(this.servicio.guardar(dbCurso));
	}
	
	
/* Este controlador es para buscar el curso por id del alumno */
	@GetMapping("/buscar-alumno-por-curso/{id}")
	public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id){
		
			Curso curso1 = servicio.findCursoByAlumnoId(id);
			
			if(curso1 != null) {
				
				/*
				 * Obtenemos los examenes respondidos del microservicio Respuestas a traves
				 * cliente HTTP Feign.
				 * 
				 * Como el metodo «obtenerExamenesIdsConRespuestasAlumno» es un Iterable
				 * se hace un CAST a List<>, para aprovechar el "contains"
				 */
				List<Long> examenesIds = (List<Long>) servicio.obtenerExamenesIdsConRespuestasAlumno(id); // (1 Feign)
				
				if (examenesIds != null && examenesIds.size() > 0) {
					
				/*
				 * API stream: sirve para manipular objetos y usar el operador "map" que permite
				 * cambiar el estado por cada objeto.
				 * 
				 * Se recibe la nueva lista «examenesNueva» modificada desde «curso1.getExamenes()».
				 */
				List<Examen> examenesNueva = curso1.getExamenes()	// Se crea nueva lista a partir de "curso1"
						.stream()
						.map(	// Permite modificar el estado del objeto.
							exm -> {
								
								/*
								 * Se valida de que «examenesIds» este contenido
								 * dentro de Examen.Id —getId()—, ejemplo: si la consulta en
								 * «findExamenesIdsConRespuestasByAlumno» viene vacio, no va estar
								 * contenido en el «getId()», por tanto se mantiene en False.
								 */
								if (examenesIds.contains(exm.getId())) {
									exm.setRespondido(true);
								}
								return exm;	// En el "map" se retorna el objeto modificado.
							}
						)
						.collect(Collectors.toList());	// Se convierte de nuevo de un "stream" a "List"
				
				/*
				 * Ahora se cambia la lista de examenes «curso1.getExamenes()» por
				 * la nueva lista «examenesNueva» usando el metodo «setExamenes».
				 */
				curso1.setExamenes(examenesNueva);
				}
			}
		return ResponseEntity.ok(curso1);
	}
	
	
/* Este controlador es para asignar el examen al curso */
	@PutMapping("/{id}/asignar-examenes") // nombre de ruta
	public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenesR, @PathVariable Long id){
		
		Optional<Curso> validacion = this.servicio.buscarById(id);
		if (!validacion.isPresent()) {
			return ResponseEntity.notFound().build();
		}

			Curso dbCurso = validacion.get();
			
		// lambda foreach
			examenesR.forEach(elemento -> {
					dbCurso.addExamen(elemento);
			});
			
			return ResponseEntity.status(HttpStatus.CREATED).body(this.servicio.guardar(dbCurso));
	}
	
	
	
	@PutMapping("/{id}/eliminar-examen") 
	public ResponseEntity<?> eliminarExamen(@RequestBody Examen examenR, @PathVariable Long id){
		
		Optional<Curso> validacion = this.servicio.buscarById(id);
		if (!validacion.isPresent()) {
			return ResponseEntity.notFound().build();
		}

			Curso dbCurso = validacion.get();
			
			dbCurso.removeExamen(examenR);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(this.servicio.guardar(dbCurso));
	}
	
	@GetMapping("/")
	@Override
	public ResponseEntity<?> listar(){
		
		/*
		 * c = Curso
		 * ca = CursoAlumno
		 * 
		 * Se convierte de un Iterable a un List y se usa el stream
		 * para pasar el alumno por cada Curso, luego se crea una instancia
		 * de Alumno para pasarle el ID. Esto para usarlo en el frontend
		 * para llenar el atributo en Curso List<Alumnos> con objetos de tipo
		 * alumno.
		 */
		List<Curso> cursos =  ((List<Curso>) servicio.buscarTodo())
				.stream()
				.map(c -> {
					c.getCursoAlumno()	// public List<CursoAlumno> getCursoAlumno() {}
					.forEach(ca -> {
						Alumno alumno = new Alumno();
						alumno.setId(ca.getAlumnoId());	// public Long getAlumnoId() {}
						c.addAlumno(alumno);	// public void addAlumno(Alumno alumno) {}
					});
					return c;
				}).collect(Collectors.toList());
		return ResponseEntity.ok().body(cursos);
	}
	
	/*
	 * Buscar un curso por id pero con bbdd distrbuida
	 */
	@GetMapping("/distribuido/{id}")
	public 	ResponseEntity<?> verDistribuido(@PathVariable Long id){ // id curso
		
		Optional<Curso> objeto = servicio.buscarById(id);
		
		if (objeto.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso curso = objeto.get();
		
		if (curso.getCursoAlumno().isEmpty() == false)
		{
			List<Long> ids = curso.getCursoAlumno()
					.stream()
					
				/*	Opción 1
					.map(ca -> {
						return ca.getAlumnoId();
					})
					
					Se puede hacer una función de flecha como opcion 2,
					cuando solo esta retornando
				*/	
					.map(ca -> ca.getAlumnoId())
					.collect(Collectors.toList());
			
			List<Alumno> alumnos = (List<Alumno>) servicio.obtenerAlumnosPorCurso(ids);
			
			curso.setAlumnos(alumnos);
		}
		return ResponseEntity.ok().body(curso);
	}
	
	/*
	 * Para listar los cursos y los ids con paginable pero con la bbdd distribuida
	 */
	@GetMapping("/pagina")
	@Override
	public ResponseEntity<?> listar(Pageable paginable){
		
		Page<Curso> cursos = servicio.buscarTodo(paginable)
				.map(curso -> {
					curso.getCursoAlumno()	// public List<CursoAlumno> getCursoAlumno() {}
					.forEach(ca -> {
						Alumno alumno = new Alumno();
						alumno.setId(ca.getAlumnoId());	// public Long getAlumnoId() {}
						curso.addAlumno(alumno);	// public void addAlumno(Alumno alumno) {}
					});
					return curso;
				});
		
		return ResponseEntity.ok().body(cursos);
	}
	
	/*
	 * Endpoint para eliminar de la bbdd un alumno del curso
	 */
	@DeleteMapping("/eliminar-alumno-del-curso/{id}")
	public ResponseEntity<?> eliminarAlumnoPorCursoId(@PathVariable Long id){
		
		servicio.eliminarCursoAlumnoPorId(id);
		return ResponseEntity.noContent().build();
	}

}
