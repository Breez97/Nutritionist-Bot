package com.breez.dispatcher_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@AllArgsConstructor
public class KafkaUpdate extends Update {

	private Message message;

}
