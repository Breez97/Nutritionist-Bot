package com.breez.dispatcher_service.config;

import com.breez.dispatcher_service.handlers.*;
import com.breez.dispatcher_service.handlers.calories.*;
import com.breez.dispatcher_service.handlers.preferencies.*;
import com.breez.dispatcher_service.handlers.diet.ChooseDietHandler;
import com.breez.dispatcher_service.handlers.diet.SetDietHandler;
import com.breez.dispatcher_service.model.UserState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StateHandlerConfig {

	@Bean
	public Map<UserState, StateHandler> stateHandlers(
			StartHandler startStateHandler,
			ChooseDietHandler chooseDietHandler,
			SetDietHandler setDietHandler,
			InfoCaloriesHandler infoCaloriesHandler,
			GetCaloriesHandler getCaloriesHandler,
			SetCaloriesHandler setCaloriesHandler,
			CountCaloriesHandler countCaloriesHandler,
			GetAgeHandler getAgeHandler,
			GetHeightHandler getHeightHandler,
			GetWeightHandler getWeightHandler,
			GetGenderHandler getGenderHandler,
			GetTargetHandler getTargetHandler,
			GetResultInfoHandler getResultInfoHandler,
			CalculateCaloriesHandler calculateCaloriesHandler) {
		return Map.ofEntries(
				Map.entry(UserState.START, startStateHandler),
				Map.entry(UserState.CHOOSE_DIET, chooseDietHandler),
				Map.entry(UserState.SET_DIET, setDietHandler),
				Map.entry(UserState.INFO_CALORIES, infoCaloriesHandler),
				Map.entry(UserState.GET_CALORIES, getCaloriesHandler),
				Map.entry(UserState.SET_CALORIES, setCaloriesHandler),
				Map.entry(UserState.COUNT_CALORIES, countCaloriesHandler),
				Map.entry(UserState.GET_AGE, getAgeHandler),
				Map.entry(UserState.GET_HEIGHT, getHeightHandler),
				Map.entry(UserState.GET_WEIGHT, getWeightHandler),
				Map.entry(UserState.GET_GENDER, getGenderHandler),
				Map.entry(UserState.GET_TARGET, getTargetHandler),
				Map.entry(UserState.GET_RESULT_INFO, getResultInfoHandler),
				Map.entry(UserState.CALCULATE_CALORIES, calculateCaloriesHandler)
		);
	}

}
