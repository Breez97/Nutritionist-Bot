package com.breez.dispatcher_service.handlers.meals;

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

import java.util.ArrayList;
import java.util.List;

@Component
public class CountMealsHandler implements StateHandler {

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
		if (update.getMessage().getText().contains("Continue")) {
			SendMessage message = messageUtils.sendTextMessage(update, "Please select the amount of meals per day you want.");
			List<String> keyboardButtons = new ArrayList<>();
			int amountOfCalories = userConfigurationService.getUserCalories(chatId);
			if (200 <= amountOfCalories && amountOfCalories <= 4000) keyboardButtons.add("1");
			if (200 <= amountOfCalories && amountOfCalories <= 8000) keyboardButtons.add("2");
			if (300 <= amountOfCalories && amountOfCalories <= 12000) keyboardButtons.add("3");
			if (400 <= amountOfCalories && amountOfCalories <= 16000) keyboardButtons.add("4");
			ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(keyboardButtons, 1);
			message.setReplyMarkup(replyKeyboardMarkup);
			telegramBotService.sendMessage(message);
			userStateService.setState(chatId, UserState.SET_MEALS);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.INFO_CALORIES);
		}
	}

}
