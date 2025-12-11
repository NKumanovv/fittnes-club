package com.fitness_club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FitnessClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessClubApplication.class, args);
	}

}
