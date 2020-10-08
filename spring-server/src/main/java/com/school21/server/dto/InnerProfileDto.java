package com.school21.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.Set;

public class InnerProfileDto {
	private String login;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("last_name")
	private String lastName;
	private String biography;
	@JsonProperty("sex")
	private String sex;
	@JsonProperty("sex_preferences")
	private String sexPreferences;
	private Float latitude;
	private Float longitude;
	private Integer age;
	private Set<String> tags;
	@JsonProperty("new_email")
	private String newEmail;
	@JsonProperty("old_password")
	private String oldPassword;
	@JsonProperty("new_password")
	private String newPassword;
	private String password;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getSexPreferences() {
		return sexPreferences;
	}

	public void setSexPreferences(String sexPreferences) {
		this.sexPreferences = sexPreferences;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public boolean hasFields() throws IllegalAccessException {
		Class<?> thisClass = this.getClass();
		for (Field field : thisClass.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.get(this) != null &&
					!"tags".equals(field.getName()) &&
					!"newEmail".equals(field.getName()) &&
					!"password".equals(field.getName()) &&
					!"oldPassword".equals(field.getName()) &&
					!"newPassword".equals(field.getName())) {
				return true;
			}
		}
		return false;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
