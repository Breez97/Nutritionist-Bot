package com.breez.count_calories.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserConfig {

	private long chatId;
	private String diet;
	private int age;
	private int height;
	private int weight;
	private String gender;
	private String target;
	private String coefficient;
	private int calories;
	private int meals;

}
