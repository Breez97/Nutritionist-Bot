package com.breez.meal_generator.service;

import com.breez.meal_generator.model.Dish;
import com.breez.meal_generator.model.UserConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaService {

	@Autowired
	private MealSuggestionService mealSuggestionService;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@KafkaListener(topics = "generate-meals-by-user-config-topic", groupId = "generate-meals-by-user-config-group")
	public void topicListener(String message) {
		try {
			UserConfig userConfig = objectMapper.readValue(message, UserConfig.class);
			List<Dish> selectedMeals = mealSuggestionService.calculateMeals(userConfig.getCalories(), userConfig.getMeals(), userConfig.getDiet());
			JSONObject jsonData = convertToJSON(userConfig, selectedMeals);
			sendToKafka(userConfig.getChatId(), jsonData);
			System.out.println(jsonData);
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}
	}

	private JSONObject convertToJSON(UserConfig userConfig, List<Dish> selectedDishes) {
		JSONObject jsonData = new JSONObject();
		jsonData.put("chatId", userConfig.getChatId());
		JSONArray dishesArray = new JSONArray();
		for (Dish dish : selectedDishes) {
			JSONObject dishObject = new JSONObject();
			dishObject.put("title", dish.getTitle());
			dishObject.put("image", dish.getImage());
			dishObject.put("calories", dish.getCalories());
			dishObject.put("carbs", dish.getCarbs());
			dishObject.put("fat", dish.getFat());
			dishObject.put("protein", dish.getProtein());
			dishObject.put("link", dish.getLink());
			dishesArray.put(dishObject);
		}
		jsonData.put("dishes", dishesArray);
		return jsonData;
	}

	private void sendToKafka(long chatId, JSONObject jsonData) {
		String message = jsonData.toString();
		kafkaTemplate.send("generated-meals-topic", String.valueOf(chatId), message);
	}

}
