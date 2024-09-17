package com.breez.dispatcher_service.service;

import com.breez.dispatcher_service.controller.BotNutritionist;
import com.breez.dispatcher_service.controller.UpdateController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramBotService {

	private final ApplicationContext applicationContext;

	@Autowired
	public TelegramBotService(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void processUpdate(Update update) {
		getUpdateController().processUpdate(update);
	}

	public void sendMessage(SendMessage message) {
		getBotNutritionist().sendAnswerMessage(message);
	}

	private BotNutritionist getBotNutritionist() {
		return applicationContext.getBean(BotNutritionist.class);
	}

	private UpdateController getUpdateController() {
		return applicationContext.getBean(UpdateController.class);
	}

}
