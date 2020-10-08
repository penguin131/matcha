package com.school21.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school21.server.dictionary.SortType;

public class UserFilterDto {
	private Integer distance;
	@JsonProperty("sex_preferences")
	private String sexPreferences;
	private String sex;
	private Integer rating;
	@JsonProperty("age_max")
	private Integer ageMax;
	@JsonProperty("age_min")
	private Integer ageMin;
	@JsonProperty("sort_type")
	private String sortType;//distance, rating, tags. Rating first default

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
		if (SortType.TAGS.getName().equals(sortType)) {
			return sortType;
		} else if (SortType.DISTANCE.getName().equals(sortType)) {
			return sortType;
		}
		return SortType.RATING.getName();
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean hasFields() {
		return distance != null || sexPreferences != null || sex != null || rating != null || ageMax != null || ageMin != null || sortType != null;
	}

	@Override
	public int hashCode() {
		if (!hasFields())
			return 0;
		int hash = 7;
		hash = 31 * hash + (distance != null ? distance : 0);
		hash = 31 * hash + (sexPreferences != null ? sexPreferences.hashCode() : 0);
		hash = 31 * hash + (sex != null ? sex.hashCode() : 0);
		hash = 31 * hash + (rating != null ? rating : 0);
		hash = 31 * hash + (ageMax != null ? ageMax : 0);
		hash = 31 * hash + (ageMin != null ? ageMin : 0);
		hash = 31 * hash + (sortType != null ? sortType.hashCode() : 0);
		return hash;
	}
}
