package com.formacionbdi.microservicios.app.usuarios.models.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.formacionbdi.microservicios.commnos.alumnos.models.entity.Alumno;



// Capa repositorio

/* También se podría usar directamente en el controlador, pero
 * no es buena practica. Lo ideal es desacoplar porque podriamos tener
 * un controlador con varios accesos a objetos repositorios. MVC
 * 
 */

//public interface AlumnoRepository extends CrudRepository<nombreClaseEntity, tipoID>


// public interface AlumnoRepository extends CrudRepository<Alumno, Long> {
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

	
	/*
	 * esta no es un consulta nativa en la bbdd; es una consulta Hibernate o JPA Query (HQL).
	 * 
	 * SELECT
	 * 	alumnos.*
	 * FROM
	 * 	alumnos
	 * WHERE
	 * 	alumnos.nombre LIKE '%andr%' OR
	 * 	alumnos.apellido LIKE '%cast%'
	 *
	 * @Query("select a from Alumno a where upper(a.nombre) like upper(concat('%', ?1, '%')) or upper(a.apellido) like upper(concat('%', ?1, '%'))")
	 */
	 
	@Query("SELECT "
			+ 		"alum "
			+ "FROM "
			+ 		"Alumno alum "
			+ "WHERE "
			+ 		"lower(alum.nombre) LIKE lower(concat('%%', :valor, '%%')) OR "
			+ 		"lower(alum.apellido) LIKE lower(concat('%%', :valor, '%%'))")
	public List<Alumno> findByNombreOrApellido(@Param("valor") String termino);
	
	/*
	 * Ordenar por Id para que se visualice en orden ascendente en el Frontend.
	 * por la bbdd Postgre
	 */
	//@Query("SELECT alm FROM Alumno alm ORDER BY alm.Id ASC")
	public Iterable<Alumno> findAllByOrderByIdAsc();
	
	//@Query("SELECT alm FROM Alumno alm ORDER BY alm.Id ASC")
	public Page<Alumno> findAllByOrderByIdAsc(Pageable paginable);
	
}
