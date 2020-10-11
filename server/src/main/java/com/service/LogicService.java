package com.service;

import com.exceptions.AccessDeniedException;
import com.exceptions.ValidateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import spark.Request;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface LogicService {
	/**
	 * Вернет массив всех профилей. Временная шняга
	 */
	String getAllUsers(String login);

	/*
	Массив всех координат
	 */
	String getAllCoordinates();

	/**
	 * Добавление нового пользовательского профиля
	 */
	void createUserProfile(Request request) throws ValidateException;

	/**
	 * Вернет первый профиль юзера по login
	 */
	String getUserProfileForLogin(String login, String from);

	/**
	 * Вернет всех друзей с последним сообщением
	 */
	String getAllFriendsForLogin(String login);


	/**
	 * Вернет всех понравившихся людей
	 */
	String getAllLikedUsers(String login);

	/**
	 * Вернет всех пользователей, посмотревших текущего
	 */
	String getAllLookedUsers(String login);

	/**
	 * Ставит лайк. Если запись уже есть в БД - она просто подтверждается.
	 */
	void setLike(String from, String to) throws ValidateException;

	/**
	 * Ставит жалобу, влияет на рейтинг
	 */
	void setComplaint(String from, String to);

	/**
	 * Ставит жалобу на фейк
	 */
	void setComplaintAboutFake(String from, String to);

	/**
	 * Удаляет профиль юзера по login
	 */
	void deleteUserProfileForLogin(String login);

	/**
	 * Обновляет пользовательский профиль
	 */
	void updateUserProfile(Request request, String login) throws ValidateException, MessagingException, AccessDeniedException;

	/**
	 * Обновляет пользовательский профиль
	 */
	void updateUserCoordinates(String requestBody, String login);

	/**
	 * Токен по данным из тела запроса
	 */
	String getToken(Request request) throws AccessDeniedException;

	/**
	 * Проставит подтвержденную почту пользователю по его токену
	 */
	String confirmUserForToken(String token);

	/**
	 * Вернет всю историю сообщений между двумя пользователями, упорядоченную по дате в обратном порядке
	 * @param user1 пользователь 1
	 * @param user2 пользователь 2
	 * @return список сообщений
	 */
	String getChatHistory(String user1, String user2);

	/**
	 * Сохранит сообщение, все данные о нем в DTO
	 */
	void saveChatMessage(String requestBody);

	/**
	 * Сохраняет в БД запись о картинке, прикрепляется к пользователю. Сохраняет картинку на сервере.
	 */
	String downloadImage(String login, byte[] image);

	byte[] getImage(String name);

	/**
	 * Удалит запись о картинке в БД
	 * @param user логин пользователя
	 * @param id ID картинки
	 * @throws AccessDeniedException Нельзя удалять чужие картинки
	 */
	void deleteImage(String user, String id) throws AccessDeniedException;

	/**
	 * Отметит картинку как аватарку
	 * @param imageId ID картинки
	 * @param userLogin Логин пользователя
	 */
	void setMainImage(String imageId, String userLogin);

	/**
	 * Вернет список всех картинок пользователя
	 * @param user Логин пользователя
	 * @return Список всех картинок одного пользователя
	 */
	String getUserPhotos(String user);

	/**
	 * Поиск пользователей по фильтру, исключая текущего юзера
	 */
	String getUsersWithFilter(String filter, String login);

	/**
	 * Получение следующего пользователя в поиске
	 * @param login логин того, кто запрашивает
	 * @param filter фильтр поиска
	 * @return JSON найденного юзера
	 */
	String getNextUser(String login, String filter);

	/**
	 * Апдейт мейла из временного поля на новый
	 * @param token логин пользователя
	 */
	String updateUserMail(String token);
}
