package com.para.crudos.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
@ConfigurationProperties("spring.datasource")
public class CrudOrdemServicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudOrdemServicoApplication.class, args);
	}

}
