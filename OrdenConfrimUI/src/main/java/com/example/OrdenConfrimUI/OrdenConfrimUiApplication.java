package com.example.OrdenConfrimUI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class OrdenConfrimUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdenConfrimUiApplication.class, args);
	}

}
