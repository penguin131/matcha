package com.service;

import com.dictionary.Sex;
import com.dto.*;
import com.entity.TUserProfileEntity;
import com.exceptions.AccessDeniedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.EntityDataHelper;
import com.helper.Password;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseServiceORMImp implements DatabaseService {
	private EntityManager em;
	private final Logger logger = Logger.getLogger(DatabaseServiceORMImp.class);
	private final ObjectMapper mapper = new ObjectMapper();

	public DatabaseServiceORMImp(EntityManager em) {
		this.em = em;
	}

	@Override
	public List<UserProfileDto> getAllUsers(String login) throws JsonProcessingException {
		List<UserProfileDto> result = getUsersWithFilter(null, login);
		if (login != null) {
			result.add(getUserProfileForLogin(login));
		}
		return result;
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
		logger.info(String.format("getUserWithFilter(%s, %s)", mapper.writeValueAsString(filter), login));
		List<UserProfileDto> profiles = new ArrayList<>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TUserProfileEntity> cQuery = cb.createQuery(TUserProfileEntity.class);
		Root<TUserProfileEntity> c = cQuery.from(TUserProfileEntity.class);
		List<Predicate> predicates = new ArrayList<>();
		//Если передан логин, то исключаю его из поиска
		if (!StringUtils.isEmpty(login)) {
			predicates.add(cb.notEqual(c.get("login"), login));
		}
		if (filter != null && filter.hasFields()) {
//			if (filter.getDistance() != null)
//				cQuery.where(cb.equal(c.get("wsg84_check_distance()"), filter.getDistance()));
			if (filter.getSexPreferences() != null)
				predicates.add(cb.equal(c.get("sexPreferences"), Sex.convertStringToCode(filter.getSexPreferences())));
			if (filter.getSex() != null)
				predicates.add(cb.equal(c.get("sex"), Sex.convertStringToCode(filter.getSex())));
			if (filter.getRating() != null)
				predicates.add(cb.greaterThanOrEqualTo(c.get("rating"), filter.getRating()));
			if (filter.getAgeMax() != null)
				predicates.add(cb.lessThanOrEqualTo(c.get("age"), filter.getAgeMax()));
			if (filter.getAgeMin() != null)
				predicates.add(cb.greaterThanOrEqualTo(c.get("age"), filter.getAgeMin()));
		}
		if (predicates.size() > 0) {
			cQuery.where(cb.and(predicates.toArray(new Predicate[0])));
		}
		for (TUserProfileEntity entity : em.createQuery(cQuery).getResultList()) {
			profiles.add(EntityDataHelper.toDto(entity));
		}
		logger.info("getUsersWithFilter() result:\n" + mapper.writeValueAsString(profiles));
		return profiles;
	}
}
