package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

import jakarta.validation.Valid;

// Como se había mencionado en secciones anteriores como esta es una aplicación solo backend con API REST 
// los controladores los anotamos con @RestController y no con @Controller.
@RestController
@RequestMapping("/api") //Ruta principal
// Damos acceso al dominio de la aplicación en Angular para que tenga acceso. Adicionalmente como segundo parámetro 
// indicando el methods podemos decirle que tipo de peticiones se pueden hacer, pero en este caso no los indicamos 
// para que por defecto se pueda hacer todo tipo de peticiones. También se pueden colocar restricciones sobre los
// header, y otras cosas que podemos buscar en internet y ver su documentación.
@CrossOrigin(origins = {"http://localhost:4200"}) 
public class ClienteRestController {
	
	// Inyectamos el servicio
	@Autowired
	private IClienteService clienteService;
	
	// Ruta para listar los clientes
	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}
	
	// Ruta para obtener un cliente por id
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			cliente = clienteService.findById(id);
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			response.put("mensaje", "Error al realizar la consulta en la base de datos. Contacte con el administrador.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null) {
			response.put("mensaje", "El cliente con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	
	}
	
	// Ruta para crear un cliente.
	// Hay que tener en cuenta que como la información viene en formato JSON dentro del cuerpo de la petición, tenemos
	// que indicar que es @RequestBody.
	@PostMapping("/clientes")
	public ResponseEntity<?> crear(@Valid @RequestBody Cliente cliente, BindingResult result) { 
		
		Cliente clienteNuevo = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			clienteNuevo = clienteService.save(cliente);
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			response.put("mensaje", "Error al realizar el insert en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
		response.put("mensaje", "El cliente ha sido creado con éxito!");
		response.put("clienteNuevo", clienteNuevo);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	// Ruta para actualizar un cliente
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> actualizar(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		Cliente clienteUpdated = null;
		
		// Obtenemos el cliente de la base de datos por su id
		Cliente clienteActual = clienteService.findById(id);
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (clienteActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el cliente con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			// Pasamos los valores modificados
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(cliente.getCreateAt());
			
			// Persistimos el cliente con los cambios realizados. Esto internamente va a hacer un merge de los
			// datos para actualizarlos y por lo tanto esto detás de escena se traduce en un update en la base
			// de datos
			clienteUpdated = clienteService.save(clienteActual);
		
		} catch (DataAccessException e) {
			e.printStackTrace();
			response.put("mensaje", "Error al actualizar el cliente en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con éxito!");
		response.put("clienteNuevo", clienteUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
	}
	
	// Ruta para eliminar un cliente por id
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		Cliente clienteAEliminar = clienteService.findById(id);
		
		try {
			
			if(clienteAEliminar == null) {
				response.put("mensaje", "Error: El cliente con ID: "+ id +" que desea eliminar no existe en la base de datos.");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			} 
			
			else {				
				clienteService.delete(id);
				response.put("mensaje", "El cliente ha sido eliminado con éxito!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK); 
			}
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			response.put("mensaje", "Error al eliminar el cliente en la base de datos.");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
	}

}
