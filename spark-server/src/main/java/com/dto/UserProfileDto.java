package com.dto;

import com.dictionary.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileDto extends BaseUserProfileDto {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private Boolean confirmed;
    private String biography;
    @JsonProperty("sex_preferences")
    private String sexPreferences;
    @JsonProperty("confirmed_token")
    private String confirmedToken;
    private float[] location;
    private int photo;
    private int rating;
    private int age;

    public String getSexPreferences() {
        return sexPreferences;
    }

    public void setSexPreferences(String sexPreferences) {
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
                          String sex,
                          String sexPreferences,
                          String biography,
                          String email,
                          Boolean confirmed,
                          String confirmedToken,
                          float[] location,
                          int photo,
                          int rating,
                          int age) {
        this.setSex(sex);
        this.setSexPreferences(sexPreferences);
        this.setBiography(biography);
        this.setPassword(password);
        this.setLogin(login);
        this.setEmail(email);
        this.setConfirmed(confirmed);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setConfirmedToken(confirmedToken);
        this.setLocation(location);
        this.photo = photo;
        this.rating = rating;
        this.age = age;
    }

    public UserProfileDto() { }

    public String getConfirmedToken() {
        return confirmedToken;
    }

    public void setConfirmedToken(String confirmedToken) {
        this.confirmedToken = confirmedToken;
    }

    public float[] getLocation() {
        return location;
    }

    public void setLocation(float[] location) {
        this.location = location;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public static UserProfileDto getInstance(ResultSet rs) throws SQLException {
        return new UserProfileDto(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("login"),
                rs.getString("password"),
                Sex.convertCodeToString(rs.getInt("sex")),
                Sex.convertCodeToString(rs.getInt("sex_preferences")),
                rs.getString("biography"),
                rs.getString("email"),
                rs.getBoolean("confirmed"),
                rs.getString("confirmed_token"),
                new float[] {rs.getFloat("location_1"), rs.getFloat("location_2")},
                rs.getInt("photo"),
                rs.getInt("rating"),
                rs.getInt("age"));
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
