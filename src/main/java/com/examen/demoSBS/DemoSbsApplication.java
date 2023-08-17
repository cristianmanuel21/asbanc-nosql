package com.examen.demoSBS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DemoSbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSbsApplication.class, args);
	}

}
