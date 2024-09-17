package com.breez.dispatcher_service.service;

import com.breez.dispatcher_service.controller.BotNutritionist;
import com.breez.dispatcher_service.controller.UpdateController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBotService {

	@Autowired
	private ApplicationContext applicationContext;

	public void processUpdate(Update update) {
		getUpdateController().processUpdate(update);
	}

	public void sendMessage(SendMessage message) {
		getBotNutritionist().sendAnswerMessage(message);
	}

	public void sendPhoto(SendPhoto sendPhoto) {
		try {
			getBotNutritionist().execute(sendPhoto);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private BotNutritionist getBotNutritionist() {
		return applicationContext.getBean(BotNutritionist.class);
	}

	private UpdateController getUpdateController() {
		return applicationContext.getBean(UpdateController.class);
	}

}
