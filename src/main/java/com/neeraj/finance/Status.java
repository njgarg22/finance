package com.neeraj.finance;

// Orders must go through a certain series of state transitions from the time a customer 
//  submits an order and it is either fulfilled or cancelled.
enum Status {

	IN_PROGRESS, //
	COMPLETED, //
	CANCELLED
}