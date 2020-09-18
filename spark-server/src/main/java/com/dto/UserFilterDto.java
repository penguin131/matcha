package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserFilterDto {
	private Integer distance;
	@JsonProperty("sex_preferences")
	private String sexPreferences;
	private String sex;
	private Integer rating;
	private Integer ageMax;
	private Integer ageMin;
	private String sortType;

	public UserFilterDto(Integer distance, String sexPreferences, String sex, Integer rating, Integer ageMax, Integer ageMin, String sortType) {
		this.distance = distance;
		this.sexPreferences = sexPreferences;
		this.sex = sex;
		this.rating = rating;
		this.ageMax = ageMax;
		this.ageMin = ageMin;
		this.sortType = sortType;
	}

	public UserFilterDto() {
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
		return distance != null || sexPreferences != null || sex != null || rating != null || ageMax != null || ageMin != null;
	}

	public Integer getAgeMax() {
		return ageMax;
	}

	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}

	public Integer getAgeMin() {
		return ageMin;
	}

	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
}
