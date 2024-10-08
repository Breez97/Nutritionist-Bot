package com.breez.dispatcher_service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class KafkaCaloriesEvent extends ApplicationEvent {

	private final String message;

	public KafkaCaloriesEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

}
