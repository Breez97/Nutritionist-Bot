package com.breez.dispatcher_service.utils;

import com.breez.dispatcher_service.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageUtils {

	@Autowired
	private TelegramBotService telegramBotService;

	public SendMessage sendTextMessage(Update update, String text) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText(text);
		return sendMessage;
	}

	public void errorMessage(Update update) {
		SendMessage sendMessage = this.sendTextMessage(update, "⚠ Incorrect answer, try again. ⚠");
		telegramBotService.sendMessage(sendMessage);
	}

}
