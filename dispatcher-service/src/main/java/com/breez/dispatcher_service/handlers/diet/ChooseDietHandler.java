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

import java.util.List;

@Component
public class ChooseDietHandler implements StateHandler {

	@Autowired
	private TelegramBotService telegramBotService;
	@Autowired
	private UserStateService userStateService;
	@Autowired
	private MessageUtils messageUtils;
	@Autowired
	private KeyboardUtils keyboardUtils;

	@Override
	public void handle(Update update, long chatId) {
		if (update.getMessage().getText().contains("Let's try")) {
			SendMessage sendMessage = messageUtils.sendTextMessage(update, "Let's start from the beginning.\n\nSelect the type of your diet.");
			ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(List.of(
					"Anything 🥪", "Paleo 🍖",
					"Vegetarian 🥦", "Vegan 🌱",
					"Ketogenic 🥙", "Mediterranean 🪔"
			), 2);
			sendMessage.setReplyMarkup(replyKeyboardMarkup);
			telegramBotService.sendMessage(sendMessage);
			userStateService.setState(chatId, UserState.SET_DIET);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.CHOOSE_DIET);
		}
	}

}
