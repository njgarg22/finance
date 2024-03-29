package com.neeraj.finance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 This class will get loaded automatically by Spring. What happens when it gets loaded?
	a) Spring Boot will run ALL `CommandLineRunner` beans once the application context is loaded.
	b) This runner will request a copy of the `EmployeeRepository` you just created.
	c) Using it, it will create two entities and store them.
*/
@Configuration
public class LoadDatabase {
	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(OrderRepository orderRepository) {

		return args -> {
			orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
			orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

			orderRepository.findAll().forEach(order -> {
				log.info("Preloaded " + order);
			});
		};
	}
}
