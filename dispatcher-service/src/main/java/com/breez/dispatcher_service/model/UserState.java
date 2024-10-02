package com.breez.dispatcher_service.model;

public enum UserState {

	START,

	// diet
	CHOOSE_DIET,
	SET_DIET,

	// calories
	INFO_CALORIES,
	GET_CALORIES,
	SET_CALORIES,
	COUNT_CALORIES,

	// calculate calories
	GET_AGE,
	GET_HEIGHT,
	GET_WEIGHT,
	GET_GENDER,
	GET_TARGET,
	GET_RESULT_INFO,
	CALCULATE_CALORIES,
	CONFIRM_CALORIES,

	// meals
	COUNT_MEALS
}
