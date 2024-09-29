package com.breez.dispatcher_service.handlers.preferencies;

import com.breez.dispatcher_service.handlers.StateHandler;
import com.breez.dispatcher_service.model.UserConfiguration;
import com.breez.dispatcher_service.model.UserState;
import com.breez.dispatcher_service.service.TelegramBotService;
import com.breez.dispatcher_service.service.UserConfigurationService;
import com.breez.dispatcher_service.service.UserStateService;
import com.breez.dispatcher_service.utils.KeyboardUtils;
import com.breez.dispatcher_service.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GetResultInfoHandler implements StateHandler {

	@Autowired
	private TelegramBotService telegramBotService;
	@Autowired
	private UserStateService userStateService;
	@Autowired
	private UserConfigurationService userConfigurationService;
	@Autowired
	private MessageUtils messageUtils;
	@Autowired
	private KeyboardUtils keyboardUtils;

	private final Set<String> VALID_COEFFICIENT = new HashSet<>(Set.of(
			"Low", "Small", "Average", "High", "Very high")
	);

	@Override
	public void handle(Update update, long chatId) {
		String answer = update.getMessage().getText();
		if (VALID_COEFFICIENT.contains(answer)) {
			SendMessage message = messageUtils.sendTextMessage(update, "Your choice: " + answer);
			userConfigurationService.setUserConfiguration(chatId, UserConfiguration.COEFFICIENT, answer);
			telegramBotService.sendMessage(message);
			SendMessage infoMessage = messageUtils.sendTextMessage(update, userConfigurationService.toString(chatId));
			infoMessage.setReplyMarkup(keyboardUtils.setKeyboard(List.of("Continue ➡"), 1));
			telegramBotService.sendMessage(infoMessage);
			userStateService.setState(chatId, UserState.CALCULATE_CALORIES);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.GET_RESULT_INFO);
		}
	}

}
