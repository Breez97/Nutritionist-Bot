package com.breez.dispatcher_service.service;

import com.breez.dispatcher_service.model.UserConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserConfigurationService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private final Map<Long, Map<UserConfiguration, Object>> userConfiguration = new HashMap<>();

	public void setUserConfiguration(Long chatId, UserConfiguration userConfigurationValue, Object value) {
		userConfiguration.computeIfAbsent(chatId, userConf -> new HashMap<>()).put(userConfigurationValue, value);
	}

	public Map<UserConfiguration, Object> getAllUserConfigurations(Long chatId) {
		return userConfiguration.getOrDefault(chatId, new HashMap<>());
	}

	public Integer getUserCalories(Long chatId) {
		Map<UserConfiguration, Object> userConfig = userConfiguration.get(chatId);
		if (userConfig != null) {
			Object caloriesObj = userConfig.get(UserConfiguration.CALORIES);
			if (caloriesObj instanceof Integer) {
				return (Integer) caloriesObj;
			}
		}
		return null;
	}

	public String toString(Long chatId) {
		Map<UserConfiguration, Object> configs = getAllUserConfigurations(chatId);
		StringBuilder resultString = new StringBuilder("Your options\n\n");
		for (UserConfiguration config : UserConfiguration.values()) {
			if (config != UserConfiguration.CALORIES && configs.containsKey(config)) {
				String configName = capitalizeFirstLetter(config.name());
				Object value = configs.get(config);
				if (value != null) {
					String valueString = formatValue(configName, value.toString());
					resultString.append(configName).append(": ").append(valueString).append("\n");
				}
			}
		}
		return resultString.toString();
	}

	public String resultInfo(Long chatId) {
		Map<UserConfiguration, Object> configs = getAllUserConfigurations(chatId);
		StringBuilder resultString = new StringBuilder("Information about choices:\n\n");
		String diet = configs.containsKey(UserConfiguration.DIET) ?
				configs.get(UserConfiguration.DIET).toString() : "Not specified";
		resultString.append("Diet: ").append(diet).append("\n");
		int calories = configs.containsKey(UserConfiguration.CALORIES) ?
				Integer.parseInt(configs.get(UserConfiguration.CALORIES).toString()) : 0;
		resultString.append("Daily calorie goal: ").append(calories).append(" cal\n");
		Object mealsObj = getAllUserConfigurations(chatId).get(UserConfiguration.MEALS);
		int meals = mealsObj != null ? Integer.parseInt(mealsObj.toString()) : 1;
		resultString.append("Number of meals: ").append(meals);
		return resultString.toString();
	}

	private String capitalizeFirstLetter(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}

	private String formatValue(String configName, String value) {
		StringBuilder resultString = new StringBuilder();
		resultString.append(value);
		switch (configName) {
			case "Height":
				resultString.append("cm");
				break;
			case "Weight":
				resultString.append("kg");
				break;
			default:
				break;
		}
		return resultString.toString();
	}

}

