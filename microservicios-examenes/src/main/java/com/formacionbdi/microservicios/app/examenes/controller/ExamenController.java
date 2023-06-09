package com.formacionbdi.microservicios.app.examenes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.microservicios.app.examenes.services.ExamenService;
import com.formacionbdi.microservicios.commnos.examenes.models.entity.Examen;
import com.formacionbdi.microservicios.comun.controllers.CommonController;

import jakarta.validation.Valid;

@RestController
public class ExamenController extends CommonController<Examen, ExamenService> {

	/*
	 * Endpoint para obtener todas las pregunta «IN (1,3,4)» junto con el Id del examen. En el repositorio esta agrupado
	 * por Id del examen.
	 * 
	 * NOTA: Es mejor usar un tipo "List" es mas concreto, que un "Iterable" «@RequestParam Iterable<Long> preguntaId»
	 */
	@GetMapping("/respondidos-por-preguntas")
	public ResponseEntity<?> obtenerExamenesIdsPorPreguntasIdRespondidas(@RequestParam List<Long> preguntaId){
		return ResponseEntity.ok().body(servicio.findExamenesIdsConRespuestasByPreguntaIds(preguntaId));
	}
	
/* ************* CONTROLADOR PARA BUSCAR EXAMEN ****************************/
	
	@PutMapping("/{id}")  
	public ResponseEntity<?> editar(@Valid @RequestBody Examen examenR, BindingResult resultado, @PathVariable Long id) {
		
		// Ahora se valida antes de editar usando el metodo "validar"
		if(resultado.hasErrors()) {
			return this.validar(resultado);
		}
		
		Optional<Examen> valorEncontrado = servicio.buscarById(id);
		
		if(!valorEncontrado.isPresent()) { // si no está presente el ID
			return ResponseEntity.notFound().build(); // 404
		}
				
		Examen examenDb = valorEncontrado.get(); // si esta presente obtenemos el examen
		
		
		// se cambia el nombre que es obtenido del request «examenR» y se puebla en el obj Examen
		examenDb.setNombre(examenR.getNombre());
		
 
/*				
		Revisar que preguntas hay en la bbdd; pero no estan en el JSON que nos estan
		enviando. Pueden haber eliminado una o varias preguntas o agregaron preguntas.
		
			
		List<Pregunta> eliminadas = new ArrayList<>();
		
=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


		+ vamos iterar por cada pregunta de la bbdd con un ForEach y vamos a preguntar,
		si esa —pregunta— existe en el JSON que nos estan enviando.

		+ se emite la pregunta «pdb» existente en la tabla.
	
		+ Si la pregunta(pdb) NO(!) existe en la preguntas que estamos recibiendo en RequestBody(ExamenR),
		entonces, se agrega al ArrayList "eliminadas".
		Y tambien va usar el metodo equals que se implemento en la Entity Pregunta
	
		+ en resumen, si no existe la pregunta en el nuevo JSON; la eliminamos de la bbdd

/* ============= OPCION 1 ======================================================================
 	
		examenDb.getPreguntas()
		.forEach(pdb -> {
			if(!examenR.getPreguntas().contains(pdb)) {
				eliminadas.add(pdb);
			}
		});
		
		
		eliminadas.forEach(pelim -> {
			examenDb.removePregunta(pelim);
		});
		
*/		
/* ============= OPCION 2 ====================================================================== */		
		
		examenDb.getPreguntas()	// obtenemos las preguntas
		
		.stream() // convertimos a un stream de Java
		
		/*
		 * Filter:
		 * Que las preguntas(pdb) que se emiten, que existen en la base de datos "contains(pdb)"
		 * pero NO(!) existen en este nuevo JSON "!examenR.getPreguntas()".
		 */
		.filter(pdb -> !examenR.getPreguntas().contains(pdb))
		
		.forEach(examenDb::removePregunta);
		
		/*
		 * List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		 * Stream<Integer> streamNumeros = numeros.stream();
		 * Stream<Integer> numerosPares = streamNumeros.filter(n -> n % 2 == 0);
		 */
		
/* =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */		
		
		/*
		 * Se modifica las preguntas existentes y se agregan las nuevas preguntas que nos envian.
		 */
		examenDb.setPreguntas(examenR.getPreguntas());
				
		return ResponseEntity.status(HttpStatus.CREATED).body(servicio.guardar(examenDb));
			
	}
	
	
	
/* ************ ENDPOINT PARA BUSCAR POR NOMBRE DE EXAMEN ****************************** */ 
	
	@GetMapping("/filtrar/{expresion}")
	public ResponseEntity<?> filtrar(@PathVariable(name= "expresion") String termino2){
		return ResponseEntity.ok(servicio.findByNombre(termino2));
	}
	
	
	
/* ************ ENDPOINT PARA LISTAR ASIGNATURAS ****************************** */
	
	@GetMapping("/asignaturas")
	public ResponseEntity<?> listarAsignaturas(){
		return ResponseEntity.ok(servicio.buscarTodasAsignatura()); // --> ExamenService
	}
	
}
