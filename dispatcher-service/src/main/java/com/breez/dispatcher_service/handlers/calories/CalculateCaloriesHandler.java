package com.breez.dispatcher_service.handlers.calories;

import com.breez.dispatcher_service.event.KafkaMessageEvent;
import com.breez.dispatcher_service.handlers.StateHandler;
import com.breez.dispatcher_service.model.KafkaUpdate;
import com.breez.dispatcher_service.model.UserConfiguration;
import com.breez.dispatcher_service.model.UserState;
import com.breez.dispatcher_service.service.KafkaService;
import com.breez.dispatcher_service.service.TelegramBotService;
import com.breez.dispatcher_service.service.UserConfigurationService;
import com.breez.dispatcher_service.service.UserStateService;
import com.breez.dispatcher_service.utils.KeyboardUtils;
import com.breez.dispatcher_service.utils.MessageUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Map;

@Component
public class CalculateCaloriesHandler implements StateHandler {

	@Autowired
	private UserConfigurationService userConfigurationService;
	@Autowired
	private KafkaService kafkaService;
	@Autowired
	private TelegramBotService telegramBotService;
	@Autowired
	private UserStateService userStateService;
	@Autowired
	private MessageUtils messageUtils;
	@Autowired
	private KeyboardUtils keyboardUtils;

	@Override
	public void handle(Update update, long chatId) {
		if (update.getMessage().getText().contains("Continue")) {
			Map<UserConfiguration, Object> userConfigurations = userConfigurationService.getAllUserConfigurations(chatId);
			JSONObject jsonData = kafkaService.convertToJson(chatId, userConfigurations);
			kafkaService.sendToKafka(chatId, jsonData);
		} else if (update instanceof KafkaUpdate) {
			handleKafkaMessage((KafkaUpdate) update, ((KafkaUpdate) update).getMessage().getText(), chatId);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.CALCULATE_CALORIES);
		}
	}

	@EventListener
	public void onKafkaMessageReceived(KafkaMessageEvent event) {
		String messageContent = event.getMessage();
		try {
			JSONObject jsonMessage = new JSONObject(messageContent);
			long chatId = jsonMessage.getLong("chatId");
			Message message = new Message();
			message.setText(messageContent);
			Chat chat = new Chat();
			chat.setId(chatId);
			message.setChat(chat);
			KafkaUpdate kafkaUpdate = new KafkaUpdate(message);
			handle(kafkaUpdate, chatId);
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}
	}

	private void handleKafkaMessage(KafkaUpdate kafkaUpdate, String message, long chatId) {
		try {
			JSONObject jsonMessage = new JSONObject(message);
			double calories = jsonMessage.getDouble("calories");
			SendMessage sendMessage = messageUtils.sendTextMessage(kafkaUpdate, "Your amount of calories: " + calories);
			ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(
					List.of("Leave this amount ✅", "Set my own value ❌"), 2);
			sendMessage.setReplyMarkup(replyKeyboardMarkup);
			telegramBotService.sendMessage(sendMessage);
			userStateService.setState(chatId, UserState.CONFIRM_CALORIES);
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}
	}
}
