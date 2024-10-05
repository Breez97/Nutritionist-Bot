package com.breez.dispatcher_service.handlers.meals;

import com.breez.dispatcher_service.event.KafkaMealsEvent;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Map;

@Component
public class GenerateMealsHandler implements StateHandler {

	@Autowired
	private UserStateService userStateService;
	@Autowired
	private TelegramBotService telegramBotService;
	@Autowired
	private UserConfigurationService userConfigurationService;
	@Autowired
	private KafkaService kafkaService;
	@Autowired
	private MessageUtils messageUtils;
	@Autowired
	private KeyboardUtils keyboardUtils;

	@Override
	public void handle(Update update, long chatId) {
		if (update.getMessage().getText().contains("Generate")) {
			SendMessage message = messageUtils.sendTextMessage(update, "Generating your menu... Just wait a few seconds ⏳");
			telegramBotService.sendMessage(message);
			Map<UserConfiguration, Object> userConfigurations = userConfigurationService.getAllUserConfigurations(chatId);
			JSONObject jsonData = kafkaService.convertToJson(chatId, userConfigurations);
			kafkaService.sendGenerateRequestToKafka(chatId, jsonData);
		} else if (update instanceof KafkaUpdate) {
			handleKafkaMessage((KafkaUpdate) update, (update).getMessage().getText(), chatId);
			SendMessage message = messageUtils.sendTextMessage(update,
					"Here is your menu, by now you can start from beginning by sending /start command or by pressing button.");
			ReplyKeyboardMarkup replyKeyboardMarkup = keyboardUtils.setKeyboard(List.of("/start"), 1);
			message.setReplyMarkup(replyKeyboardMarkup);
			telegramBotService.sendMessage(message);
			userStateService.setState(chatId, UserState.START);
		} else {
			messageUtils.errorMessage(update);
			userStateService.setState(chatId, UserState.GENERATE_MEALS);
		}
	}

	@EventListener
	public void onKafkaMessageReceived(KafkaMealsEvent event) {
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
			JSONArray dishes = jsonMessage.getJSONArray("dishes");
			for (int i = 0; i < dishes.length(); i++) {
				JSONObject dish = dishes.getJSONObject(i);
				String title = dish.getString("title");
				String imageUrl = dish.getString("image");
				int calories = dish.getInt("calories");
				double carbs = dish.getDouble("carbs");
				double protein = dish.getDouble("protein");
				double fat = dish.getDouble("fat");
				String link = dish.getString("link");
				String dishMessage = String.format(
						"🌟 *%s*\n" +
								"Calories: %d kcal\n" +
								"Carbs: %.1f g\n" +
								"Protein: %.1f g\n" +
								"Fat: %.1f g\n" +
								"Link: [View Recipe](https://www.eatthismuch.com%s)",
						title, calories, carbs, protein, fat, link
				);
				SendPhoto sendPhoto = createPhotoForDish(kafkaUpdate, imageUrl, dishMessage);
				telegramBotService.sendPhoto(sendPhoto);
			}
		} catch (JSONException e) {
			System.out.println("JSON parsing error: " + e.getMessage());
		}
	}

	private SendPhoto createPhotoForDish(Update update, String imageUrl, String message) {
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setChatId(update.getMessage().getChatId());
		sendPhoto.setCaption(message);
		sendPhoto.setPhoto(new InputFile(imageUrl));
		sendPhoto.setReplyMarkup(keyboardUtils.removeKeyboard());
		return sendPhoto;
	}

}
