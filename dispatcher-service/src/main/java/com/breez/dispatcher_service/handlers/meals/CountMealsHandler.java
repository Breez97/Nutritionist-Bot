package com.breez.dispatcher_service.handlers.meals;

import com.breez.dispatcher_service.handlers.StateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CountMealsHandler implements StateHandler {

	@Override
	public void handle(Update update, long chatId) {

	}

}
