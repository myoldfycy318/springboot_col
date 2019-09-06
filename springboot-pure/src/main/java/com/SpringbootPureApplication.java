package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.UUID;

@SpringBootApplication
public class SpringbootPureApplication {

    public static void main(String[] args) {
        System.setProperty("appconfig.customerTopic", "test" + UUID.randomUUID().toString());
        SpringApplication.run(SpringbootPureApplication.class, args);
    }
}
