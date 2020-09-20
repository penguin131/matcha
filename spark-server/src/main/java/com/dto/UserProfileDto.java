package com.dto;

import com.dictionary.Sex;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileDto {
    private String login;
    private String email;
    private String sex;
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
    @JsonProperty("tags")
    private List<String> hashTags;

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
        UserProfileDto user = new UserProfileDto(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("login"),
                Sex.convertCodeToString(rs.getInt("sex")),
                Sex.convertCodeToString(rs.getInt("sex_preferences")),
                rs.getString("biography"),
                rs.getString("email"),
                rs.getBoolean("confirmed"),
                rs.getString("confirmed_token"),
                rs.getInt("photo"),
                rs.getInt("rating"),
                rs.getInt("age"),
                rs.getFloat("latitude"),
                rs.getFloat("longitude"),
                rs.getInt("has_like") > 0,
                rs.getInt("has_dislike") > 0
        );
        List<String> tags = new ArrayList<>();
        tags.add("music");
        tags.add("dance");
        tags.add("drink vodka");
        user.setHashTags(tags);
        return user;
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
                          Float longitude,
                          Boolean hasLike,
                          Boolean hasDislike) {
        this.setSex(sex);
        this.setSexPreferences(sexPreferences);
        this.setBiography(biography);
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
        this.hasLike = hasLike;
        this.hasDislike = hasDislike;
    }

    public UserProfileDto() {
        latitude = (float) 0;
        longitude = (float) 0;
        photo = -1;
        confirmed = false;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
