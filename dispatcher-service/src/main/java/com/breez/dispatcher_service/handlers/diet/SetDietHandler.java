package com.breez.dispatcher_service.handlers.diet;

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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SetDietHandler implements StateHandler {

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

	private final Set<String> VALID_DIETS = new HashSet<>(Set.of(
			"Anything 🥪", "Paleo 🍖", "Vegetarian 🥦", "Vegan 🌱", "Ketogenic 🥙", "Mediterranean 🪔"
	));

	@Override
	public void handle(Update update, long chatId) {
		String answer = update.getMessage().getText();
		if (VALID_DIETS.contains(answer)) {
			SendMessage message = messageUtils.sendTextMessage(update, "Your choice: " + answer);
			ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(List.of("Continue ➡"), 1);
			message.setReplyMarkup(replyKeyboardMarkup);
			telegramBotService.sendMessage(message);
			userConfigurationService.setUserConfiguration(chatId, UserConfiguration.DIET, answer);
			userStateService.setState(chatId, UserState.INFO_CALORIES);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.SET_DIET);
		}
	}

}
