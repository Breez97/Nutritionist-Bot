package com.breez.dispatcher_service.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface StateHandler {

	void handle(Update update, long chatId);
}
