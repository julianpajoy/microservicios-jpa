package com.formacionbdi.microservicios.app.cursos.models.enity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.formacionbdi.microservicios.commnos.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.commnos.examenes.models.entity.Examen;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="cursos") // asi se llamara la tabla en la bbdd
public class Curso {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty	// Esta anotacion viene de la dependencia de "Spring Web"
	private String nombre;
	
	@Column(name="create_at") // cambiara el nombre del campo en la bbdd
	@Temporal(TemporalType.TIMESTAMP) // pondra la fecha completa con hora
	private Date createAt;
	
	
/* 
 * Como van a ser varios alumnos asociados a un curso
 * se hace un ArrayList.
 * 
 * Se agrega la anotacion "@OneToMany" para indicar "un curso a muchos alumnos".
 * 
 * El parámetro "fetch = FetchType.LAZY" indica que los datos de la lista
 * de alumnos no se cargarán automáticamente de la base de datos cuando
 * se acceda a la entidad que contiene esta declaración de variable, 
 * sino que se cargarán solo cuando sea necesario. Esto mejora el rendimiento 
 * de la aplicación al reducir la cantidad de datos que se cargan en la memoria.
 */


/*
 * Esto es para la base de datos compartida (MySQL).
 * 
 * Se añade la relacion en curso con sus alumnos.
 * Curso es el dueño de la relacion y contiene la lista de Alumnos.
 */
	// @OneToMany(fetch = FetchType.LAZY) mapear | cardinalidad
	@Transient // No esta mapeado a la tabla, atributo se va a poblar.
	private List<Alumno> alumnos; // atributo de tipo list
	
	/*
	 * Relacion con entidad intermedia CursoAlumno, ya que la base de datos
	 * estan aparte PostgreSQL y MySQL.
	 * 
	 * mappedBy -> relacion bidireccional
	 * 
	 * cascade: si se elimina un curso se elimine la relacion con los alumnos
	 */
	@JsonIgnoreProperties(value = {"curso"}, allowSetters = true)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CursoAlumno> cursoAlumno;
	
	
/* ================================================================================== */

	// Se agrega un nuevo atributo para examenes
	@ManyToMany(fetch = FetchType.LAZY) // Un curso puede tener muchos examenes y un examen puede estar en muchos cursos
	private List<Examen> examenes;
	
	
	/*
	 * establece automaticamente en la fecha actual antes de que se persista la
	 * entidad en la base de datos
	 */
	@PrePersist
	public void prePersist() {
		this.createAt= new Date();
	}
	
	public Curso() { // constructor para inicializar el array list.
		//List<Alumno> alumnos = new ArrayList<>();
		this.alumnos= new ArrayList<>();
		this.examenes = new ArrayList<>();
		this.cursoAlumno = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	
/* ============================================================================ */	
	
	public List<Alumno> getAlumnos() {
		return alumnos; 
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
	
	public void addAlumno(Alumno alumno) { // se crea metodo para crear un solo alumno.
		this.alumnos.add(alumno);
	}
	
	public void removeAlumno(Alumno alumno) {
		this.alumnos.remove(alumno);
	}
	
	
/* ============================================================================ */

	public List<Examen> getExamenes() {
		return examenes;
	}

	public void setExamenes(List<Examen> examenes) {
		this.examenes = examenes;
	}
	
	public void addExamen(Examen examen) {
		this.examenes.add(examen);
	}
	
	public void removeExamen(Examen examen) {
		this.examenes.remove(examen);
	}

/* ============================================================================ */	

	public List<CursoAlumno> getCursoAlumno() {
		return cursoAlumno;
	}

	public void setCursoAlumno(List<CursoAlumno> cursoAlumno) {
		this.cursoAlumno = cursoAlumno;
	}
	
	public void addCursoAlumno(CursoAlumno cursoAlumno) {
		this.cursoAlumno.add(cursoAlumno);
	}
	
	public void removeCursoAlumno(CursoAlumno cursoAlumno) {
		this.cursoAlumno.remove(cursoAlumno);
	}
	
	
		
}
