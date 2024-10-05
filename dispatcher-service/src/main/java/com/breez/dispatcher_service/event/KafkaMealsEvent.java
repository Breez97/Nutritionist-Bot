package com.breez.dispatcher_service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class KafkaMealsEvent extends ApplicationEvent {

	private final String message;

	public KafkaMealsEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

}
