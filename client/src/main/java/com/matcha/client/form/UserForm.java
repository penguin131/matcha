package com.matcha.client.form;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * в нем может быть больше данных чем в RegisterForm
 */
public class UserForm {
    private String login;
    private int confirmed;
    @JsonProperty("sex_preferences")
    private int sexPreferences;
    private String biography;

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
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

    public UserForm(String login,
                    int confirmed,
                    int sexPreferences,
                    String biography) {
        this.login = login;
        this.confirmed = confirmed;
        this.sexPreferences = sexPreferences;
        this.biography = biography;
    }

    public UserForm() {

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
