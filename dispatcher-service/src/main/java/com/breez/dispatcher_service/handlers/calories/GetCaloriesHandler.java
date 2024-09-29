package com.breez.dispatcher_service.handlers.calories;

import com.breez.dispatcher_service.handlers.StateHandler;
import com.breez.dispatcher_service.model.UserState;
import com.breez.dispatcher_service.service.TelegramBotService;
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
public class GetCaloriesHandler implements StateHandler {

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
		if (update.getMessage().getText().equals("Yes ✅")) {
			SendMessage message = messageUtils.sendTextMessage(update, "Input the amount of calories.");
			message.setReplyMarkup(keyboardUtils.removeKeyboard());
			telegramBotService.sendMessage(message);
			userStateService.setState(chatId, UserState.SET_CALORIES);
		} else if (update.getMessage().getText().equals("No ❌")) {
			SendMessage message = messageUtils.sendTextMessage(update, "Let's count how many calories do you need.");
			ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(List.of("Count 🔢"), 1);
			message.setReplyMarkup(replyKeyboardMarkup);
			telegramBotService.sendMessage(message);
			userStateService.setState(chatId, UserState.COUNT_CALORIES);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.GET_CALORIES);
		}
	}

}
