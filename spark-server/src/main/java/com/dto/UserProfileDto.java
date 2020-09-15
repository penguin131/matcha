package com.dto;

import com.dictionary.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;

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
    private Float latitude;
    private Float longitude;
    private Integer photo;
    private Integer rating;
    private Integer age;
    @JsonProperty("has_like")
    private Boolean hasLike;
    @JsonProperty("has_dislike")
    private Boolean hasDislike;

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

    public String getConfirmedToken() {
        return confirmedToken;
    }

    public void setConfirmedToken(String confirmedToken) {
        this.confirmedToken = confirmedToken;
    }

    public Integer getPhoto() {
        return photo;
    }

    public void setPhoto(Integer photo) {
        this.photo = photo;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public Boolean getHasLike() {
        return hasLike;
    }

    public void setHasLike(Boolean hasLike) {
        this.hasLike = hasLike;
    }

    public Boolean getHasDislike() {
        return hasDislike;
    }

    public void setHasDislike(Boolean hasDislike) {
        this.hasDislike = hasDislike;
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
                rs.getInt("photo"),
                rs.getInt("rating"),
                rs.getInt("age"),
                rs.getFloat("location_1"),
                rs.getFloat("location_2"));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserProfileDto))
            return false;
        UserProfileDto other = (UserProfileDto) obj;
        return super.equals(other) &&
                StringUtils.equals(firstName, other.getFirstName()) &&
                StringUtils.equals(lastName, other.getLastName()) &&
                StringUtils.equals(biography, other.getBiography()) &&
                StringUtils.equals(sexPreferences, other.getSexPreferences()) &&
                StringUtils.equals(confirmedToken, other.getConfirmedToken()) &&
                integerEquals(photo, other.getPhoto()) &&
                integerEquals(rating, other.getRating()) &&
                integerEquals(age, other.getAge());
    }

    private boolean integerEquals(Integer b1, Integer b2) {
        if (b1 != null) {
            return b1.equals(b2);
        }
        return b2 == null;
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
                          Integer photo,
                          Integer rating,
                          Integer age,
                          Float latitude,
                          Float longitude) {
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
        this.photo = photo;
        this.rating = rating;
        this.age = age;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserProfileDto() {
        latitude = (float) 0;
        longitude = (float) 0;
        photo = -1;
        confirmed = false;
    }
}
