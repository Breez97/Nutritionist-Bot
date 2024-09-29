package com.breez.dispatcher_service.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardUtils {

	public ReplyKeyboardMarkup setKeyboard(List<String> keyboardStrings, int buttonsPerRow) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardRow row = new KeyboardRow();
		for (int i = 0; i < keyboardStrings.size(); i++) {
			if (i > 0 && i % buttonsPerRow == 0) {
				keyboard.add(row);
				row = new KeyboardRow();
			}
			row.add(keyboardStrings.get(i));
		}
		if (!row.isEmpty()) {
			keyboard.add(row);
		}
		keyboardMarkup.setKeyboard(keyboard);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);
		return keyboardMarkup;
	}

	public ReplyKeyboardRemove removeKeyboard() {
		ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
		replyKeyboardRemove.setRemoveKeyboard(true);
		return replyKeyboardRemove;
	}
}
