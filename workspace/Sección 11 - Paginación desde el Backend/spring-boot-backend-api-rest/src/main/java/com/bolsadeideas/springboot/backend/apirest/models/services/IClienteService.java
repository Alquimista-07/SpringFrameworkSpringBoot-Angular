package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

// Esta interface contiene el contrato de implementación, es decir, los métodos del CRUD
public interface IClienteService {

	// Listar todo
	public List<Cliente> findAll();
	
	// Buscar cliente por id
	public Cliente findById(Long id);
	
	// Guardar cliente
	public Cliente save(Cliente cliente);
	
	// Borrar cliente
	public void delete(Long id);
	
}
