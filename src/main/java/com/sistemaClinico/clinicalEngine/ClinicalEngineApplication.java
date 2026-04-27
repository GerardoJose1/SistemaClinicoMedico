package com.sistemaClinico.clinicalEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ClinicalEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicalEngineApplication.class, args);
	}

}
