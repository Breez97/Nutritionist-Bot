package com.breez.dispatcher_service.controller;

import com.breez.dispatcher_service.handlers.StateHandler;
import com.breez.dispatcher_service.model.UserState;
import com.breez.dispatcher_service.service.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class UpdateController {

	@Autowired
	private UserStateService userStateService;
	@Autowired
	private Map<UserState, StateHandler> stateHandlers;

	public void processUpdate(Update update) {
		if (update == null) {
			throw new RuntimeException("Error update");
		}

		if (update.hasMessage() && update.getMessage().hasText()) {
			processTextMessage(update);
		} else {
			System.out.println("Received unsupported message type " + update);
		}
	}

	private void processTextMessage(Update update) {
		long chatId = update.getMessage().getChatId();
		UserState currentState = userStateService.getState(chatId);
		StateHandler handler = stateHandlers.get(currentState);

		if (handler != null) {
			handler.handle(update, chatId);
		} else {
			System.out.println("No handler found for state: " + currentState);
		}
	}
}