package com.example.demoMysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.demoMysql.dao")
@ComponentScan("com.example.demoMysql")
public class DemoMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMysqlApplication.class, args);
	}
}
