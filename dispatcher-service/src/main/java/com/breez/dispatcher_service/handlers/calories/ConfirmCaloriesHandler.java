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
public class ConfirmCaloriesHandler implements StateHandler {

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
		String answer = update.getMessage().getText();
		if (answer.contains("Leave this amount")) {
			SendMessage message = messageUtils.sendTextMessage(update, "Ok, fine. I get it.");
			ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(List.of("Continue ➡"), 1);
			message.setReplyMarkup(replyKeyboardMarkup);
			telegramBotService.sendMessage(message);
			userStateService.setState(chatId, UserState.COUNT_MEALS);
		} else if (answer.contains("Set my own value")) {

		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.CONFIRM_CALORIES);
		}
	}

}
