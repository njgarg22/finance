package com.neeraj.finance;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// @RestController indicates that the data returned by each method will be written 
//  straight into the response body instead of rendering a template.
@RestController
class EmployeeController {

	private final EmployeeRepository repository;

	// An EmployeeRepository is injected by constructor into the controller.
	EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	// Aggregate root

	/*
	 * To make the aggregate root more RESTful, we should include top level links
	 * while ALSO including any RESTful components within.
	 */
	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>> all() {

		List<EntityModel<Employee>> employees = repository.findAll().stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
						linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
				.collect(Collectors.toList());

		return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}

	@PostMapping("/employees")
	Employee newEmployee(@RequestBody Employee newEmployee) {
		return repository.save(newEmployee);
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

		/*
		 * Our project is based on Spring MVC and uses the static helper methods from
		 * WebMvcLinkBuilder to build the links.
		 * 
		 * Spring HATEOAS build a link to the EmployeeController's one() method, and
		 * flag it as a self link.
		 * 
		 * Also, Spring HATEOAS build a link to the aggregate root, all(), and call it
		 * "employees".
		 * 
		 * What do we mean by "build a link"? One of Spring HATEOAS’s core types is
		 * Link. It includes a URI and a rel (relation). Links are what empower the web.
		 * Before the World Wide Web, other document systems would render information or
		 * links. But it was the linking of documents WITH this kind of relationship
		 * metadata that stitched the web together. Roy Fielding encourages building
		 * APIs with the same techniques that made the web successful, and links are one
		 * of them.
		 */
		return EntityModel.of(employee, //
				linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
	}

	/*
	 * In this situation, `replace` is a better description than `update`. For
	 * example, if the "name" is NOT provided in request body, it would instead get
	 * nulled out.
	 */
	@PutMapping("/employees/{id}")
	Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

		return repository.findById(id).map(employee -> {
			employee.setName(newEmployee.getName());
			employee.setRole(newEmployee.getRole());
			return repository.save(employee);
		}).orElseGet(() -> {
			newEmployee.setId(id);
			return repository.save(newEmployee);
		});
	}

	@DeleteMapping("/employees/{id}")
	void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
