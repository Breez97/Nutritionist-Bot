package com.breez.dispatcher_service.service;

import com.breez.dispatcher_service.event.KafkaCaloriesEvent;
import com.breez.dispatcher_service.event.KafkaMealsEvent;
import com.breez.dispatcher_service.model.UserConfiguration;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@KafkaListener(topics = "counted-calories-by-user-config-topic", groupId = "counted-calories-by-user-config-group")
	public void topicCaloriesListener(String message) {
		eventPublisher.publishEvent(new KafkaCaloriesEvent(this, message));
	}

	@KafkaListener(topics = "generated-meals-topic", groupId = "generated-meals-group")
	public void topicMealsListener(String message) {
		eventPublisher.publishEvent(new KafkaMealsEvent(this, message));
	}

	public JSONObject convertToJson(long chatId, Map<UserConfiguration, Object> userConfigurations) {
		JSONObject jsonData = new JSONObject();
		jsonData.put("chatId", chatId);
		for (Map.Entry<UserConfiguration, Object> entry : userConfigurations.entrySet()) {
			jsonData.put(entry.getKey().name().toLowerCase(), entry.getValue());
		}
		return jsonData;
	}

	public void sendConfigToKafka(long chatId, JSONObject jsonData) {
		String message = jsonData.toString();
		kafkaTemplate.send("calories-by-user-config-topic", String.valueOf(chatId), message);
	}

	public void sendGenerateRequestToKafka(long chatId, JSONObject jsonData) {
		String message = jsonData.toString();
		kafkaTemplate.send("generate-meals-by-user-config-topic", String.valueOf(chatId), message);
	}

}
