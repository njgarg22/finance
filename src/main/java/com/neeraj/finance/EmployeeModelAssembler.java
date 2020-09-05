package com.neeraj.finance;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/*
 * You need to define a function that converts Employee objects to EntityModel<Employee> objects. 
 * While you could easily code this method yourself, there are benefits down the road 
 * of implementing Spring HATEOAS’s RepresentationModelAssembler interface.
 * 
 * This simple interface has one method: toModel(). 
 * It is based on converting a non-model object (Employee) into a model-based object (EntityModel<Employee>). 
 * 
 * Spring HATEOAS’s abstract base class for all models is RepresentationModel. 
 * But for simplicity, I recommend using EntityModel<T> as your mechanism to easily wrap all POJOs as models.
 * 
 * And by applying Spring Framework’s @Component, this component will be automatically created when the app starts.
 */
@Component
class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

	@Override
	public EntityModel<Employee> toModel(Employee employee) {

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
				linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
	}
}
