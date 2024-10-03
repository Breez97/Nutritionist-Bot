package com.breez.count_calories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CountCaloriesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CountCaloriesServiceApplication.class, args);
	}

}
