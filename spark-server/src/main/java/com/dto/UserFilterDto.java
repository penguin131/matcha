package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserFilterDto {
	private Integer distance;
	@JsonProperty("sex_preferences")
	private String sexPreferences;
	private String sex;
	private Integer rating;

	public UserFilterDto(Integer distance,
						 String sexPreferences,
						 String sex,
						 Integer rating) {
		this.distance = distance;
		this.sexPreferences = sexPreferences;
		this.sex = sex;
		this.rating = rating;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public String getSexPreferences() {
		return sexPreferences;
	}

	public void setSexPreferences(String sexPreferences) {
		this.sexPreferences = sexPreferences;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return String.format("{distance: %d, sex_preferences: %s, sex: %s, rating: %d}", distance, sexPreferences, sex, rating);
	}

	public boolean hasFields() {
		return distance != null || sexPreferences != null || sex != null || rating != null;
	}
}
