package com.breez.count_calories.service;

import com.breez.count_calories.model.UserConfig;
import org.springframework.stereotype.Service;

@Service
public class CountService {

	public double calculateDailyCalories(UserConfig userConfig) {
		double amount;
		if (userConfig.getGender().toLowerCase().contains("male")) {
			amount = 10 * userConfig.getWeight() + 6.25 * userConfig.getHeight() - 5 * userConfig.getAge() + 5;
		} else {
			amount = 10 * userConfig.getWeight() + 6.25 * userConfig.getHeight() - 5 * userConfig.getAge() - 161;
		}
		double energy = amount * getActivityMultiplier(userConfig.getCoefficient());
		double targetCalories = adjustForTarget(energy, userConfig.getTarget());
		return Math.round(targetCalories);
	}

	private double getActivityMultiplier(String coefficient) {
		return switch (coefficient.toLowerCase()) {
			case "low" -> 1.2;
			case "small" -> 1.38;
			case "average" -> 1.55;
			case "high" -> 1.73;
			case "very high" -> 2.2;
			default -> 1;
		};
	}

	private static double adjustForTarget(double energy, String target) {
		if (target.contains("Weight loss")) {
			return energy * 0.85;
		} else if (target.contains("Maintaining weight")) {
			return energy * 1.15;
		} else {
			return energy * 1.5;
		}
	}

}
