package com.breez.count_calories.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserConfig {

	private long chatId;
	private String gender;
	private String coefficient;
	private int weight;
	private String diet;
	private int age;
	private String target;
	private int height;

}
