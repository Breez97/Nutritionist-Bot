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

@Component
public class GetAgeHandler implements StateHandler {

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
			int age = Integer.parseInt(answer);
			if (age >= 0 && age <= 100) {
				SendMessage message = messageUtils.sendTextMessage(update, "Your age: " + age);
				message.setReplyMarkup(keyboardUtils.removeKeyboard());
				SendMessage messageHeight = messageUtils.sendTextMessage(update, "Now input your height in cm.");
				userConfigurationService.setUserConfiguration(chatId, UserConfiguration.AGE, age);
				telegramBotService.sendMessage(message);
				telegramBotService.sendMessage(messageHeight);
				userStateService.setState(chatId, UserState.GET_HEIGHT);
			} else {
				messageUtils.errorMessage(update);
				userStateService.setState(chatId, UserState.GET_AGE);
			}
		} catch (NumberFormatException e) {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.GET_AGE);
		}
	}

}
