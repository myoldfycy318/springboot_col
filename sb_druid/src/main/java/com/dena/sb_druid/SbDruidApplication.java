package com.dena.sb_druid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan({"com.dena.sb_druid.dao"})
public class SbDruidApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbDruidApplication.class, args);
	}
}
