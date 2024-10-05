package com.breez.meal_generator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "nutrition_dishes")
public class Dish {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String image;
	private String title;
	private int calories;
	private float carbs;
	private float fat;
	private float protein;
	private String link;

}
