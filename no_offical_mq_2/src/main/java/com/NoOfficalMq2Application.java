package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
@SpringBootApplication
public class NoOfficalMq2Application {

	public static void main(String[] args) {
		SpringApplication.run(NoOfficalMq2Application.class, args);
	}
}
