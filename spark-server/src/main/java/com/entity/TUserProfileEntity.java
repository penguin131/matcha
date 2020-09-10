package com.entity;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_user_profile", schema = "spark_db", catalog = "postgres")
public class TUserProfileEntity {
	private int userProfileId;
	private String login;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private boolean confirmed;
	private String biography;
	private Integer sex;
	private Integer sexPreferences;
	private String confirmedToken;
	private BigDecimal location1;
	private BigDecimal location2;
	private Integer rating;
	private Integer age;
	private List<TImageEntity> photos;

	@Id
	@Column(name = "user_profile_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(int userProfileId) {
		this.userProfileId = userProfileId;
	}

	@Basic
	@Column(name = "\"login\"", nullable = false)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Basic
	@Column(name = "\"password\"", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Basic
	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Basic
	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Basic
	@Column(name = "confirmed",
			nullable = false,
			columnDefinition="boolean default true")
	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	@Basic
	@Column(name = "biography")
	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	@Basic
	@Column(name = "sex", nullable = false)
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Basic
	@Column(name = "sex_preferences")
	public Integer getSexPreferences() {
		return sexPreferences;
	}

	public void setSexPreferences(Integer sexPreferences) {
		this.sexPreferences = sexPreferences;
	}

	@Basic
	@Column(name = "confirmed_token")
	public String getConfirmedToken() {
		return confirmedToken;
	}

	public void setConfirmedToken(String confirmedToken) {
		this.confirmedToken = confirmedToken;
	}

	@Basic
	@Column(name = "location_1")
	public BigDecimal getLocation1() {
		return location1;
	}

	public void setLocation1(BigDecimal location1) {
		this.location1 = location1;
	}

	@Basic
	@Column(name = "location_2")
	public BigDecimal getLocation2() {
		return location2;
	}

	public void setLocation2(BigDecimal location2) {
		this.location2 = location2;
	}

	@Basic
	@Column(name = "rating", columnDefinition="integer default 0")
	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	@Basic
	@Column(name = "age")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY, mappedBy="user")
	public List<TImageEntity> getPhotos() {
		return photos;
	}

	public void setPhotos(List<TImageEntity> photos) {
		this.photos = photos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TUserProfileEntity that = (TUserProfileEntity) o;
		return userProfileId == that.userProfileId &&
				confirmed == that.confirmed &&
				sex.equals(that.sex) &&
				Objects.equals(login, that.login) &&
				Objects.equals(password, that.password) &&
				Objects.equals(email, that.email) &&
				Objects.equals(firstName, that.firstName) &&
				Objects.equals(lastName, that.lastName) &&
				Objects.equals(biography, that.biography) &&
				Objects.equals(sexPreferences, that.sexPreferences) &&
				Objects.equals(confirmedToken, that.confirmedToken) &&
				Objects.equals(location1, that.location1) &&
				Objects.equals(location2, that.location2) &&
				Objects.equals(rating, that.rating) &&
				Objects.equals(age, that.age) &&
				ArrayUtils.isEquals(photos, that.getPhotos());
	}

	@Override
	public int hashCode() {
		return Objects.hash(userProfileId, login, password, email, firstName, lastName, confirmed, biography, sex, sexPreferences, confirmedToken, location1, location2, rating, age);
	}

	public TUserProfileEntity() {
		location1 = new BigDecimal(0);
		location2 = new BigDecimal(0);
	}
}
