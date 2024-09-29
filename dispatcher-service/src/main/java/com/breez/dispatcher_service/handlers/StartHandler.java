package com.breez.dispatcher_service.handlers;

import com.breez.dispatcher_service.model.UserState;
import com.breez.dispatcher_service.service.TelegramBotService;
import com.breez.dispatcher_service.service.UserStateService;
import com.breez.dispatcher_service.utils.KeyboardUtils;
import com.breez.dispatcher_service.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Component
public class StartHandler implements StateHandler {

	@Autowired
	private TelegramBotService telegramBotService;
	@Autowired
	private UserStateService userStateService;
	@Autowired
	private MessageUtils messageUtils;
	@Autowired
	private KeyboardUtils keyboardUtils;

	@Value("${telegram.message.startMessage}")
	private String startMessage;
	@Value("${telegram.message.photoUrl}")
	private String photoUrl;

	@Override
	public void handle(Update update, long chatId) {
		if (update.getMessage().getText().startsWith("/start")) {
			SendPhoto sendPhoto = createWelcomeMessage(update);
			telegramBotService.sendPhoto(sendPhoto);
			userStateService.setState(chatId, UserState.CHOOSE_DIET);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.START);
		}
	}

	private SendPhoto createWelcomeMessage(Update update) {
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setChatId(update.getMessage().getChatId().toString());
		sendPhoto.setCaption(startMessage);
		sendPhoto.setPhoto(new InputFile(photoUrl));
		ReplyKeyboardMarkup keyboardMarkup = keyboardUtils.setKeyboard(List.of("Let's try ⏩"), 1);
		sendPhoto.setReplyMarkup(keyboardMarkup);
		return sendPhoto;
	}

}