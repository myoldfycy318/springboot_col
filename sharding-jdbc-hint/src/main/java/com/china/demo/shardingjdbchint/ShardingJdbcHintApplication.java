package com.china.demo.shardingjdbchint;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.china.demo.shardingjdbchint.repository"})
public class ShardingJdbcHintApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShardingJdbcHintApplication.class, args);
	}

}
