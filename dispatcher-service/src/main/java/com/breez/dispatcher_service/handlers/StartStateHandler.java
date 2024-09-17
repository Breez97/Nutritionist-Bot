package com.breez.dispatcher_service.handlers;

import com.breez.dispatcher_service.model.UserState;
import com.breez.dispatcher_service.service.TelegramBotService;
import com.breez.dispatcher_service.service.UserStateService;
import com.breez.dispatcher_service.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartStateHandler implements StateHandler {

	@Autowired
	private TelegramBotService telegramBotService;
	@Autowired
	private UserStateService userStateService;
	@Autowired
	private MessageUtils messageUtils;

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
			SendMessage sendMessage = messageUtils.sendTextMessage(update, "Please use /start command to begin or " +
					"press the 'Let's try ⏩' button.");
			telegramBotService.sendMessage(sendMessage);
		}
	}

	private SendPhoto createWelcomeMessage(Update update) {
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setChatId(update.getMessage().getChatId().toString());
		sendPhoto.setCaption(startMessage);
		sendPhoto.setPhoto(new InputFile(photoUrl));

		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardRow row = new KeyboardRow();
		row.add("Let's try ⏩");
		keyboard.add(row);

		keyboardMarkup.setKeyboard(keyboard);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);
		sendPhoto.setReplyMarkup(keyboardMarkup);

		return sendPhoto;
	}

}