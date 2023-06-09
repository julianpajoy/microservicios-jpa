package com.formacionbdi.microservicios.app.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
 * @SpringBootApplication, de forma automatica agrega los "Entity", los registra
 * los que esten dentro de este package base.
 * 
 * Pero como el package "Commons-Alumnos" que contiene los Entity, es distintos 
 * al packege "Usuario", por tanto, se tiene que agregar el Entity de "Commons-Alumnos"
 * a este paquete y se hace con
 * "@EntityScan"
 */

@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.formacionbdi.microservicios.commnos.alumnos.models.entity"})
public class MicroserviciosUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosUsuariosApplication.class, args);
	}

}
