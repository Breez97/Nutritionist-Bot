package com.breez.dispatcher_service.service;

import com.breez.dispatcher_service.model.UserState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserStateService {

	private final Map<Long, UserState> userStates = new HashMap<>();

	public void setState(Long chatId, UserState state) {
		userStates.put(chatId, state);
	}

	public UserState getState(Long chatId) {
		return userStates.getOrDefault(chatId, UserState.START);
	}

}