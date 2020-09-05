package com.neeraj.finance;

import org.springframework.data.jpa.repository.JpaRepository;

/*This interface extends Spring Data JPA’s JpaRepository, specifying the domain type as `Employee` and the id type as `Long`. 
  This interface, though empty on the surface, packs a punch given it supports:
	1. Creating new instances
	2. Updating existing ones
	3. Deleting
	4. Finding (one, all, by simple or complex properties)*/
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
