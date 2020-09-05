package com.neeraj.finance;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * This is a Spring MVC REST controller that actually produces hypermedia-powered content! 
 * Clients that don’t speak HAL (http://stateless.co/hal_specification.html) can ignore the extra bits 
 * while consuming the pure data. 
 * Clients that DO speak HAL can navigate your empowered API.
 * 
 * @RestController indicates that the data returned by each method will be written 
 * straight into the response body instead of rendering a template.
 */
@RestController
class EmployeeController {

	private final EmployeeRepository repository;

	private final EmployeeModelAssembler assembler;

	// An EmployeeRepository is injected by constructor into the controller.
	EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	// Aggregate root

	/*
	 * To make the aggregate root more RESTful, we should include top level links
	 * while ALSO including any RESTful components within.
	 * 
	 * CollectionModel<> is an Spring HATEOAS container aimed at encapsulating
	 * collections. It, too, also lets you include links.
	 * 
	 * Don’t let that first statement slip by. What does "encapsulating collections"
	 * mean? Collections of employees? Not quite.
	 * 
	 * Since we’re talking REST, it should encapsulate collections of employee
	 * resources. That’s why you fetch all the employees, but then transform them
	 * into a list of EntityModel<Employee> objects. (Thanks Java 8 Stream API!)
	 */
	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>> all() {

		List<EntityModel<Employee>> employees = repository.findAll().stream().map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}

	@PostMapping("/employees")
	ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

		// After saving the new Employee object, the resulting object is wrapped using
		// the EmployeeModelAssembler.
		EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

		/*
		 * Spring MVC’s ResponseEntity is used to create an HTTP 201 Created status
		 * message. This type of response typically includes a Location response header,
		 * and we use the URI derived from the model’s self-related link.
		 * 
		 * Using the getRequiredLink() method, you can retrieve the Link created by the
		 * EmployeeModelAssembler with a SELF rel. This method returns a Link which must
		 * be turned into a URI with the toUri method.
		 * 
		 * Additionally, return the model-based version of the saved object.
		 */
		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	// Single item

	/*
	 * The return type of the method is changed from Employee to
	 * EntityModel<Employee>.
	 * 
	 * EntityModel<T> is a generic container from Spring HATEOAS that includes not
	 * only the data but a collection of links.
	 */
	@GetMapping("/employees/{id}")
	EntityModel<Employee> one(@PathVariable Long id) {
		// `EmployeeNotFoundException` is an exception used to indicate when an employee
		// is looked up but not found.
		Employee employee = repository.findById(id) //
				.orElseThrow(() -> new EmployeeNotFoundException(id));

		return assembler.toModel(employee);
	}

	/*
	 * In this situation, `replace` is a better description than `update`. For
	 * example, if the "name" is NOT provided in request body, it would instead get
	 * nulled out.
	 */
	@PutMapping("/employees/{id}")
	ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

		Employee updatedEmployee = repository.findById(id) //
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				}) //
				.orElseGet(() -> {
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});

		EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	@DeleteMapping("/employees/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
		
		// returns an HTTP 204 No Content response
		return ResponseEntity.noContent().build();
	}
}
