package com.breez.meal_generator.service;

import com.breez.meal_generator.model.Dish;
import com.breez.meal_generator.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MealSuggestionService {

	@Autowired
	private DishRepository mealRepository;

	public List<Dish> calculateMeals(int totalCalories, int numberOfDishes, String preferredDietType) {
		List<Dish> allDishes = mealRepository.findAll();
		Collections.shuffle(allDishes);
		List<Dish> filteredDishes = allDishes.stream()
				.filter(dish -> matchesDietType(dish, preferredDietType))
				.toList();
		if (filteredDishes.isEmpty()) {
			return new ArrayList<>();
		}
		int caloriesPerDish = totalCalories / numberOfDishes;
		List<Dish> selectedDishes = new ArrayList<>();
		int currentCalories = 0;
		for (Dish dish : filteredDishes) {
			if (selectedDishes.size() < numberOfDishes && currentCalories + dish.getCalories() <= caloriesPerDish * numberOfDishes) {
				selectedDishes.add(dish);
				currentCalories += dish.getCalories();
			}
			if (selectedDishes.size() == numberOfDishes) {
				break;
			}
		}
		return selectedDishes;
	}

	private boolean matchesDietType(Dish dish, String preferredDietType) {
		return switch (preferredDietType) {
			case "Anything 🥪" -> true;
			case "Paleo 🍖" -> dish.getCarbs() < 30 && dish.getProtein() > 15;
			case "Vegetarian 🥦" -> dish.getCarbs() > 20 && dish.getFat() < 20 && dish.getProtein() > 10;
			case "Vegan 🌱" -> dish.getCarbs() > 40 && dish.getFat() < 10 && dish.getProtein() > 5;
			case "Ketogenic 🥙" -> dish.getCarbs() < 10 && dish.getFat() > 20;
			case "Mediterranean 🪔" -> dish.getCarbs() < 50 && dish.getFat() > 10 && dish.getProtein() > 15;
			default -> true;
		};
	}

}
