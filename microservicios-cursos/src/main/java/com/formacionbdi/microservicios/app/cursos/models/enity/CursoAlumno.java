package com.formacionbdi.microservicios.app.cursos.models.enity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="curso_alumnos")
public class CursoAlumno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/*
	 * unique, sirve para indicar que un solo alumno puede estar
	 * registrado en un curso; no debe estar duplicado.
	 */
	@Column(name="alumno_id", unique = true)
	private Long alumnoId; // almacena los ids de los alumnos pertenecientes al curso.
	
	/*
	 * Se realiza una relacion bidireccional para que no se genere otra
	 * tabla intermedia, ya que esta entidad es la tabla intermedia.
	 */
	@JsonIgnoreProperties(value = {"cursoAlumno"}) // se ignora de la relacion inversa.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id") // nombre de la foreing key
	private Curso curso; // relacion con curso

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) { // los objetos son iguales
			return true;
		}
		
		// Si el objeto NO es una instancia de CursoAlumno, entonces retornamos Falso.
		if(!(obj instanceof CursoAlumno)) {
			return false;
		}
		
		// Si es una instancia se comparan los id's
		CursoAlumno a = (CursoAlumno) obj; 
		
		
		// que el alumndoId sea disntinto de nulo y ya que es distinto de nulo se compara el id del objeto "a"
		return this.alumnoId != null && this.alumnoId.equals(a.getAlumnoId());

	}
	
}
