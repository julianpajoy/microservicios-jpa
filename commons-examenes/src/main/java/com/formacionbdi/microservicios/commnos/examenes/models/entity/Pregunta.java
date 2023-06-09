package com.formacionbdi.microservicios.commnos.examenes.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="preguntas")
public class Pregunta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String texto;

	
	/*
	 * Esta Entity es la duenia de la relacion por el @JoinColumn que tiene
	 * la llave foranea que establece la relacion entre una pregunta y el examen.
	 * 
	 * @JoinColumn: establece un relacion bidireccional entre las entidades «Examen» y «Pregunta» y
	 * establece el comportamiento de eliminacion en cascada
	 * 
	 * ManyToOne: muchas preguntas estan asociadas a una solo examen
	 */
	
	@JsonIgnoreProperties(value={"preguntas"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="examen_id")	// se establece llave foranea y se creara el campo examen_id en la bbdd
	private Examen examen;	// variable miembro, referencia a una instancia de la clase Examen.
	
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
	
/* ================================================================================== */

	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) { // los objetos son iguales
			return true;
		}
		if(!(obj instanceof Pregunta)) {
			return false;
// Si el objeto NO es una instancia de Pregunta, entonces retornamos falso.

		}
		
// Si es una instancia se comparan los id's
		Pregunta a= (Pregunta) obj;
		
		return this.id != null && this.id.equals(a.getId());
// que el ID sea distinto de nulo; y ya que es distinto de nulo se compara el id del objeto "a"
	}
	
	
	
	

}
