package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilterDto {

	private float[] location;
	@JsonProperty("sex_preferences")
	private String sexPreferences;
	private String sex;

}
