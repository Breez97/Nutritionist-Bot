package com.breez.meal_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class MealGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealGeneratorApplication.class, args);
	}

}
