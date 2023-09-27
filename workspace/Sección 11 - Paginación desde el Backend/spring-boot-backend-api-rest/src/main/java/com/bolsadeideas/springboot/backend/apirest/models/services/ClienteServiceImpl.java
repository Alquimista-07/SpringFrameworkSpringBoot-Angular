package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IClienteDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

// Implementación de los métodos del servicio.

// Lo registramos en el contenedor de spring con la antoación @Service
@Service
public class ClienteServiceImpl implements IClienteService {
	
	// Inyectamos el DAO
	@Autowired
	private IClienteDao clienteDao;

	// Implementación del método listar todo.
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	// Implementación del método de buscar cliente por id.
	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	// Implementación del método para guardar un cliente.
	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	// Implementación del método para borrar un cliente por id.
	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);		
	}

}
