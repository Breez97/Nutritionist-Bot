package com.breez.dispatcher_service.controller;

import com.breez.dispatcher_service.service.TelegramBotService;
import com.breez.dispatcher_service.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateController {

	private final MessageUtils messageUtils;
	private final ApplicationContext applicationContext;

	@Autowired
	public UpdateController(MessageUtils messageUtils, ApplicationContext applicationContext) {
		this.messageUtils = messageUtils;
		this.applicationContext = applicationContext;
	}

	public void processUpdate(Update update) {
		if (update == null) {
			throw new RuntimeException("Error update");
		}

		if (update.hasMessage()) {
			processMessage(update);
		} else if (update.hasCallbackQuery()) {
			processCallbackQuery(update);
		} else {
			System.out.println("Received unsupported message type " + update);
		}
	}

	private void processMessage(Update update) {
		if (update.getMessage().hasText()) {
			processTextMessage(update);
		} else {
			sendDefaultResponse(update);
		}
	}

	private void processTextMessage(Update update) {
		String messageText = update.getMessage().getText();

		if (messageText.startsWith("/start")) {
			SendMessage sendMessage = messageUtils.sendTextMessage(update, "Welcome! How can I help you?");
			getTelegramBotService().sendMessage(sendMessage);
		} else {
			SendMessage sendMessage = messageUtils.sendTextMessage(update, "I received your message: " + messageText);
			getTelegramBotService().sendMessage(sendMessage);
		}
	}

	private void processCallbackQuery(Update update) {
		String callbackData = update.getCallbackQuery().getData();
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(String.valueOf(chatId));
		sendMessage.setText("You selected: " + callbackData);
		getTelegramBotService().sendMessage(sendMessage);
	}

	private void sendDefaultResponse(Update update) {
		SendMessage sendMessage = messageUtils.sendTextMessage(update, "I don't know how to process this type of message yet.");
		getTelegramBotService().sendMessage(sendMessage);
	}

	private TelegramBotService getTelegramBotService() {
		return applicationContext.getBean(TelegramBotService.class);
	}

}
