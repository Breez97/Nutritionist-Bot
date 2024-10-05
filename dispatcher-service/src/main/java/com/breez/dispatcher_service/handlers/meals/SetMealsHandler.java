package com.breez.dispatcher_service.handlers.meals;

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
public class SetMealsHandler implements StateHandler {

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

	private final Set<String> VALID_MEALS = new HashSet<>(Set.of("1", "2", "3", "4"));

	@Override
	public void handle(Update update, long chatId) {
		String answer = update.getMessage().getText();
		if (VALID_MEALS.contains(answer)) {
			SendMessage infoMessage = messageUtils.sendTextMessage(update, "Your amount of meals: " + answer + " meals");
			userConfigurationService.setUserConfiguration(chatId, UserConfiguration.MEALS, answer);
			telegramBotService.sendMessage(infoMessage);
			SendMessage message = messageUtils.sendTextMessage(update, userConfigurationService.resultInfo(chatId));
			ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(List.of("Generate"), 1);
			message.setReplyMarkup(replyKeyboardMarkup);
			telegramBotService.sendMessage(message);
			userStateService.setState(chatId, UserState.GENERATE_MEALS);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.SET_MEALS);
		}
	}

}
