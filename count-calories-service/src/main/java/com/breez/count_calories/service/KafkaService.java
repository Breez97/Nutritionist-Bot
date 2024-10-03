package com.breez.count_calories.service;

import com.breez.count_calories.model.UserConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

	@Autowired
	private CountService countService;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@KafkaListener(topics = "calories-by-user-config-topic", groupId = "calories-by-user-config-group")
	public void topicListener(String message) {
		try {
			UserConfig userConfig = objectMapper.readValue(message, UserConfig.class);
			JSONObject jsonData = convertToJSON(userConfig);
			sendToKafka(userConfig.getChatId(), jsonData);
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}
	}

	private JSONObject convertToJSON(UserConfig userConfig) {
		JSONObject jsonData = new JSONObject();
		jsonData.put("chatId", userConfig.getChatId());
		jsonData.put("calories", countService.calculateDailyCalories(userConfig));
		return jsonData;
	}

	private void sendToKafka(long chatId, JSONObject jsonData) {
		String message = jsonData.toString();
		kafkaTemplate.send("counted-calories-by-user-config-topic", String.valueOf(chatId), message);
	}

}
