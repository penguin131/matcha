package com.matcha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class UserProfileDto {
    @NotNull
    private int sex;
    @JsonProperty("sex_preferences")
    private int sexPreferences;
    private String biography;
    @NotNull
    private String password;
    @NotNull
    private String login;
    @Email
    private String email;
    @Null
    private int confirmed;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
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
                          int confirmed) {
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
