package com.dean.springamqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource("applicationContext.xml")
@SpringBootApplication
public class SpringAmqpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAmqpApplication.class, args);
	}
}
