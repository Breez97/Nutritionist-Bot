package com.breez.dispatcher_service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class KafkaMessageEvent extends ApplicationEvent {

	private final String message;

	public KafkaMessageEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

}
