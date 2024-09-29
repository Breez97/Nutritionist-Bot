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

@Component
public class CountCaloriesHandler implements StateHandler {

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
		if (update.getMessage().getText().contains("Count")) {
			SendMessage message = messageUtils.sendTextMessage(update, "Ok, let's count.\n\n" +
					"First of all. Input your age.");
			message.setReplyMarkup(keyboardUtils.removeKeyboard());
			telegramBotService.sendMessage(message);
			userStateService.setState(chatId, UserState.GET_AGE);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.COUNT_CALORIES);
		}
	}

}
