package com.bolsadeideas.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

// La interface CrudRepository nos permite hacer operaciones CRUD de una manera más sencilla y rápida. 
// Para usarla solo la heredamos, le pasamos el entity y el tipo de dato del id de dicho entity y ya
// con esto podemos usar los métodos que necesitemos y que ya están implementados, por lo tanto solo
// los llamamos y los usamos. Para más información sobre la interface CrudRepository podemos ir a la 
// documentación Oficial: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
public interface IClienteDao extends CrudRepository<Cliente, Long> {

	// También acá podemos implementar nuestros propios método y que anotemos con @Query para realizar consultas 
	// propias personalizadas.
	
}
