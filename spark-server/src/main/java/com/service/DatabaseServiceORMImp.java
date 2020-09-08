package com.service;

import com.dictionary.Sex;
import com.dto.*;
import com.entity.TUserProfileEntity;
import com.exceptions.AccessDeniedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.EntityDataHelper;
import com.helper.Password;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseServiceORMImp implements DatabaseService {
	private EntityManager em = Persistence.createEntityManagerFactory("Model").createEntityManager();
	private final Logger logger = Logger.getLogger(DatabaseServiceORMImp.class);
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public List<UserProfileDto> getAllUsers(String login) throws JsonProcessingException {
		return getUsersWithFilter(null, login);
	}

	@Override
	public void createUserProfile(BaseUserProfileDto userProfileDto, String confirmed_token) throws Exception {

	}

	@Override
	public UserProfileDto getUserProfileForLogin(String login) throws JsonProcessingException {
		logger.info("getUserProfileForLogin() login: " + login);
		TypedQuery<TUserProfileEntity> q = em.createQuery(
				"SELECT c FROM TUserProfileEntity c where c.login=:login", TUserProfileEntity.class)
				.setParameter("login", login);
		TUserProfileEntity first = q.getResultList().stream().findFirst().orElse(null);
		UserProfileDto user = EntityDataHelper.toDto(first);
		logger.info("user: " + mapper.writeValueAsString(user));
		return user;
	}

	@Override
	public List<FriendDto> getAllFriendsForLogin(String login) throws SQLException {
		return null;
	}

	@Override
	public void setLike(String from, String to) throws Exception {

	}

	@Override
	public void setComplaint(String from, String to) throws SQLException {

	}

	@Override
	public void deleteUserProfileForLogin(String login) throws SQLException {

	}

	@Override
	public void updateUserProfile(UserProfileDto userProfileDto) throws SQLException, JsonProcessingException {

	}

	@Override
	public boolean checkPassword(String login, String password) throws Exception {
		logger.info(String.format("checkPassword() login: %s, password: %s", login, password));
		UserProfileDto user = getUserProfileForLogin(login);
		if (user == null) {
			throw new Exception("Invalid login + password");
		}
		return Password.check(password, user.getPassword());
	}

	@Override
	public void confirmUserForToken(String token) throws SQLException {

	}

	@Override
	public List<MessageDto> getChatHistory(String user1, String user2) throws SQLException {
		return null;
	}

	@Override
	public void saveChatMessage(MessageDto messageDto) throws Exception {

	}

	@Override
	public String saveImage(String user) throws SQLException {
		return null;
	}

	@Override
	public void deleteImage(String user, String id) throws SQLException, AccessDeniedException {

	}

	@Override
	public void setMainImage(String imageId, String userLogin) throws SQLException {

	}

	@Override
	public List<UserPhotoDto> getUserPhotos(String user) throws SQLException {
		return null;
	}

	@Override
	public List<UserProfileDto> getUsersWithFilter(UserFilterDto filter, String login) throws JsonProcessingException {
		logger.info(String.format("getUserWithFilter(%s)", filter == null ? "null" : filter.toString()));
		List<UserProfileDto> profiles = new ArrayList<>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TUserProfileEntity> cQuery = cb.createQuery(TUserProfileEntity.class);
		Root<TUserProfileEntity> c = cQuery.from(TUserProfileEntity.class);
		if (filter != null && filter.getDistance() != null)
			cQuery.where(cb.equal(c.get("wsg84_check_distance()"), filter.getDistance()));
		if (filter != null && filter.getSexPreferences() != null)
			cQuery.where(cb.equal(c.get("sexPreferences"), Sex.convertStringToCode(filter.getSexPreferences())));
		if (filter != null && filter.getSex() != null)
			cQuery.where(cb.equal(c.get("sex"), Sex.convertStringToCode(filter.getSex())));
		if (filter != null && filter.getRating() != null)
			cQuery.where(cb.greaterThanOrEqualTo(c.get("rating"), filter.getRating()));
		if (filter != null && filter.getAge() != null)
			cQuery.where(cb.greaterThanOrEqualTo(c.get("age"), filter.getAge()));
		for (TUserProfileEntity entity : em.createQuery(cQuery).getResultList()) {
			profiles.add(EntityDataHelper.toDto(entity));
		}
		logger.info("getUsersWithFilter() result:\n" + mapper.writeValueAsString(profiles));
		return profiles;
	}
}
