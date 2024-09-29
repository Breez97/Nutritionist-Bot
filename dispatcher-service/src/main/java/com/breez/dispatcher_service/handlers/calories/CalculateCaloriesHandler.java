package com.breez.dispatcher_service.handlers.calories;

import com.breez.dispatcher_service.handlers.StateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CalculateCaloriesHandler implements StateHandler {

	@Override
	public void handle(Update update, long chatId) {
		if (update.getMessage().getText().contains("Continue")) {

		}
	}

}
