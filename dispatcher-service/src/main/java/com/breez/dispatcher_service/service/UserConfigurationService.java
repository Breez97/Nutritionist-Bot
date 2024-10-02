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

	public String toString(Long chatId) {
		Map<UserConfiguration, Object> configs = getAllUserConfigurations(chatId);
		StringBuilder resultString = new StringBuilder();
		resultString.append("Your options\n\n");
		for (UserConfiguration config : UserConfiguration.values()) {
			String configName = capitalizeFirstLetter(config.name());
			String value = configs.get(config).toString();
			String valueString = formatValue(configName, value);
			resultString.append(configName).append(": ").append(valueString).append("\n");
		}
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

