package com.formacionbdi.microservicios.app.examenes.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

import com.formacionbdi.microservicios.commnos.examenes.models.entity.Examen;

public interface ExamenRepository extends JpaRepository<Examen, Long>{
	

	// exm: alias u objeto.
	@Query("select exm from Examen exm where exm.nombre like %?1%")
	public List<Examen> findByNombre(String termino);
	
	@Query("SELECT e.id "
			+ "FROM Pregunta p "
			+ "JOIN p.examen e "
			+ "WHERE p.id IN ?1 "
			+ "GROUP BY e.id")
	public Iterable<Long> findExamenesIdsConRespuestasByPreguntaIds(Iterable<Long> preguntaIds);
/*
 * Este metodo es mas seguro, ya que podria evitar ataques de inyeccion de SQL.
 * 
	@Query("select exm from Examen exm where exm.nombre like %:nombre%")
	List<Examen> findByNombre(@Param(value="nombre") String nombre);
*/	
}
