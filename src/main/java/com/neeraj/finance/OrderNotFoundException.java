package com.neeraj.finance;

public class OrderNotFoundException extends RuntimeException {

	OrderNotFoundException(Long id) {
		super("Could not find employee " + id);
	}
}

