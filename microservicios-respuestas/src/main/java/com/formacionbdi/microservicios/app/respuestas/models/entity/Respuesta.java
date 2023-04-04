package com.formacionbdi.microservicios.app.respuestas.models.entity;

import com.formacionbdi.microservicios.commnos.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commnos.examenes.models.entity.Pregunta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="respuestas")
public class Respuesta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;

/*	
*	Relación de base de datos compartida MariaDB 
*	@ManyToOne(fetch = FetchType.LAZY)	// Muchas respuesta; un alumno (un alumno puede tener varias respuestas)
*	private Alumno alumno; // atributo para relacion
*/	
	
//	Relacion base de datos distribuida MariaDB y PostgreSQL
	@Transient
	private Alumno alumno;
	
	@Column(name = "alumno_id")
	private Long alumnoId;
	
	@OneToOne(fetch = FetchType.LAZY)	// Una respuesta; una pregunta
	private Pregunta pregunta;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
/* ************ GETTERS Y SETTER DE LOS ATRIBUTOS Alumno y Pregunta ***************** */
	
	public Alumno getAlumno() {
		return alumno;
	}
	
	public void setAlumno(Alumno alumnoR) {
		this.alumno = alumnoR;
	}
	
	public Pregunta getPregunta() {
		return pregunta;
	}
	
	public void setPregunta(Pregunta preguntaR) {
		this.pregunta = preguntaR;
	}
	
/* +++++++++++++++++++++++++++++++++++++++++++++++++ */
	
	public Long getAlumnoId() {
		return alumnoId;
	}
	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	
}
