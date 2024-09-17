package com.breez.dispatcher_service.configuration;

import com.breez.dispatcher_service.handlers.*;
import com.breez.dispatcher_service.model.UserState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StateHandlerConfiguration {

	@Bean
	public Map<UserState, StateHandler> stateHandlers(
			StartStateHandler startStateHandler) {
		return Map.of(
				UserState.START, startStateHandler
		);
	}

}
