package com.neeraj.finance;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// JPA annotation to make this object ready for storage in a JPA-based data store.
@Entity
class Employee {
	
	// It’s the primary key and automatically populated by the JPA provider.
	private @Id @GeneratedValue Long id;
	private String name;
	private String role;

	Employee() {
	}

	Employee(String name, String role) {
		this.name = name;
		this.role = role;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getRole() {
		return this.role;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Employee))
			return false;
		Employee employee = (Employee) o;
		
		// Objects.equals returns true if the arguments are equal to each other and false otherwise. 
		// Consequently, if both arguments are null, true is returned and if exactly one argument is null, false is returned. 
		// Otherwise, equality is determined by using the equals method of the first argument.
		return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name)
				&& Objects.equals(this.role, employee.role);
	}

	@Override
	public int hashCode() {
		// Objects.hash generates a hash code for a sequence of input values. 
		// The hash code is generated as if all the input values were placed into an array.
		// And that array were hashed by calling Arrays.hashCode(Object[]).
		return Objects.hash(this.id, this.name, this.role);
	}

	@Override
	public String toString() {
		return "Employee{" + "id=" + this.id + ", name='" + this.name + '\'' + ", role='" + this.role + '\'' + '}';
	}
}
