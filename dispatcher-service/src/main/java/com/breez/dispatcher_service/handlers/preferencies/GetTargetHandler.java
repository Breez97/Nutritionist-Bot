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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GetTargetHandler implements StateHandler {

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

	@Value("${telegram.message.coefficientMessage}")
	private String coefficientMessage;

	private final Set<String> VALID_TARGETS = new HashSet<>(Set.of(
			"Weight loss 🧘‍♀️", "Maintaining weight ⏲️", "Weight gain 💪"
	));

	@Override
	public void handle(Update update, long chatId) {
		String answer = update.getMessage().getText();
		if (VALID_TARGETS.contains(answer)) {
			SendMessage message = messageUtils.sendTextMessage(update, "Your target: " + answer);
			userConfigurationService.setUserConfiguration(chatId, UserConfiguration.TARGET, answer);
			telegramBotService.sendMessage(message);
			SendMessage messageInfoCoefficient = messageUtils.sendTextMessage(update, coefficientMessage);
			messageInfoCoefficient.setParseMode("HTML");
			messageInfoCoefficient.setReplyMarkup(keyboardUtils.setKeyboard(List.of("Low", "Small", "Average", "High",
					"Very high"), 1));
			telegramBotService.sendMessage(messageInfoCoefficient);
			userStateService.setState(chatId, UserState.GET_RESULT_INFO);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.GET_TARGET);
		}
	}

}
