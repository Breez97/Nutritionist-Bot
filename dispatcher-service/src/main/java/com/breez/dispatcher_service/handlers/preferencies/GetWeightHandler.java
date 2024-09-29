package com.breez.dispatcher_service.handlers.preferencies;

import com.breez.dispatcher_service.handlers.StateHandler;
import com.breez.dispatcher_service.model.UserConfiguration;
import com.breez.dispatcher_service.model.UserState;
import com.breez.dispatcher_service.service.TelegramBotService;
import com.breez.dispatcher_service.service.UserConfigurationService;
import com.breez.dispatcher_service.service.UserStateService;
import com.breez.dispatcher_service.utils.KeyboardUtils;
import com.breez.dispatcher_service.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class GetWeightHandler implements StateHandler {

	@Autowired
	private TelegramBotService telegramBotService;
	@Autowired
	private UserStateService userStateService;
	@Autowired
	private UserConfigurationService userConfigurationService;
	@Autowired
	private MessageUtils messageUtils;
	@Autowired
	private KeyboardUtils keyboardUtils;

	@Override
	public void handle(Update update, long chatId) {
		String answer = update.getMessage().getText();
		try {
			int weight = Integer.parseInt(answer);
			if (weight >= 40 && weight <= 150) {
				SendMessage message = messageUtils.sendTextMessage(update, "Your weight in kg: " + weight);
				userConfigurationService.setUserConfiguration(chatId, UserConfiguration.WEIGHT, weight);
				SendMessage messageGender = messageUtils.sendTextMessage(update, "Choose your gender.");
				messageGender.setReplyMarkup(keyboardUtils.setKeyboard(List.of("Male 👨", "Female 👩"), 2));
				telegramBotService.sendMessage(message);
				telegramBotService.sendMessage(messageGender);
				userStateService.setState(chatId, UserState.GET_GENDER);
			} else {
				messageUtils.errorMessage(update);
				userStateService.setState(chatId, UserState.GET_WEIGHT);
			}
		} catch (NumberFormatException e) {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.GET_WEIGHT);
		}
	}

}
