package com.matcha.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfileDto {

    @JsonIgnoreProperties
    private int userProfileId;//уникальный
    private int sex;
    @JsonProperty("sex_preferences")
    private int sexPreferences;
    private String biography;
    private String password;
    private String login;
    private String email;

    public int getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(int userProfileId) {
        this.userProfileId = userProfileId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object another) {
        if (!super.equals(another)) {
            return false;
        }
        return this.userProfileId == ((UserProfileDto)another).userProfileId;
    }

    @Override
    public int hashCode() {
        return this.userProfileId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
