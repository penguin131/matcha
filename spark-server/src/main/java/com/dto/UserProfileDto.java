package com.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfileDto extends BaseUserProfileDto {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private Boolean confirmed;
    private String biography;
    @JsonProperty("sex_preferences")
    private int sexPreferences;

    public int getSexPreferences() {
        return sexPreferences;
    }

    public void setSexPreferences(int sexPreferences) {
        this.sexPreferences = sexPreferences;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
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

    public UserProfileDto(String firstName,
                          String lastName,
                          String login,
                          String password,
                          int sex,
                          int sexPreferences,
                          String biography,
                          String email,
                          Boolean confirmed) {
        this.setSex(sex);
        this.setSexPreferences(sexPreferences);
        this.setBiography(biography);
        this.setPassword(password);
        this.setLogin(login);
        this.setEmail(email);
        this.setConfirmed(confirmed);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public UserProfileDto() { }
}
