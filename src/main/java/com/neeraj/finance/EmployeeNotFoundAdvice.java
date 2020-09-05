package com.neeraj.finance;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

// When an `EmployeeNotFoundException` is thrown, this extra tidbit of Spring MVC configuration is used to render an `HTTP 404`:
@ControllerAdvice
class EmployeeNotFoundAdvice {

	@ResponseBody //signals that this advice is rendered straight into the response body.
	@ExceptionHandler(EmployeeNotFoundException.class) // configures the advice to only respond if an EmployeeNotFoundException is thrown.
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String employeeNotFoundHandler(EmployeeNotFoundException ex) {
		return ex.getMessage();
	}
}
