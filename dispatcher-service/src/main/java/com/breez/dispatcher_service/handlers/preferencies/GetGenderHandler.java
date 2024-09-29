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
public class GetGenderHandler implements StateHandler {

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

	private final Set<String> VALID_GENDERS = new HashSet<>(Set.of(
			"Male 👨", "Female 👩"
	));

	@Override
	public void handle(Update update, long chatId) {
		String answer = update.getMessage().getText();
		if (VALID_GENDERS.contains(answer)) {
			SendMessage message = messageUtils.sendTextMessage(update, "Your gender: " + answer);
			SendMessage messageTarget = messageUtils.sendTextMessage(update, "Choose your target.");
			messageTarget.setReplyMarkup(keyboardUtils.setKeyboard(List.of("Weight loss 🧘‍♀️", "Maintaining weight ⏲️",
					"Weight gain 💪"), 1));
			userConfigurationService.setUserConfiguration(chatId, UserConfiguration.GENDER, answer);
			telegramBotService.sendMessage(message);
			telegramBotService.sendMessage(messageTarget);
			userStateService.setState(chatId, UserState.GET_TARGET);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.GET_GENDER);
		}
	}

}
