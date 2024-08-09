package com.toolrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;



@SpringBootApplication
@Slf4j
public class ToolrentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToolrentalApplication.class, args);
		log.info("Tool Rental Application Started Successfully!");
	}

}
