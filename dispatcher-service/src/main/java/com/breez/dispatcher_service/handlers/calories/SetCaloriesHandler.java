package com.breez.dispatcher_service.handlers.calories;

import com.breez.dispatcher_service.handlers.StateHandler;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class SetCaloriesHandler implements StateHandler {

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
			int amountOfCalories = Integer.parseInt(answer);
			if (amountOfCalories >= 200 && amountOfCalories <= 16000) {
				ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(List.of("Continue ➡"), 1);
				SendMessage message = messageUtils.sendTextMessage(update, "Your amount of calories: " + amountOfCalories);
				message.setReplyMarkup(replyKeyboardMarkup);
				telegramBotService.sendMessage(message);
//				userStateService.setState(chatId, UserState.COUNT_MEALS);
			} else if (amountOfCalories < 200) {
				SendMessage message = messageUtils.sendTextMessage(update, "Please input more calories.");
				telegramBotService.sendMessage(message);
				userStateService.setState(chatId, UserState.SET_CALORIES);
			} else {
				SendMessage message = messageUtils.sendTextMessage(update, "Please input less calories.");
				telegramBotService.sendMessage(message);
				userStateService.setState(chatId, UserState.SET_CALORIES);
			}
		} catch (NumberFormatException e) {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.SET_CALORIES);
		}
	}

}
