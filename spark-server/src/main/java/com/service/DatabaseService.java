package com.service;

import com.dto.*;
import com.exceptions.AccessDeniedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.dto.UserPhotoDto;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    /**
     * Вернет массив всех профилей. Временная шняга
     * @return List<UserProfileDto>
     */
    List<UserProfileDto> getAllUsers(String login) throws SQLException, JsonProcessingException;

    /**
     * Добавление нового пользовательского профиля
     */
    void createUserProfile(BaseUserProfileDto userProfileDto, String confirmedToken) throws Exception;

    /**
     * Вернет первый профиль юзера по login
     * @param login login in PathVariable
     * @return UserProfileDto
     */
    UserProfileDto getUserProfileForLogin(String login) throws SQLException, JsonProcessingException;

    /**
     * Вернет всех друзей с последним сообщением
     * @param login login
     * @return List<FriendDto>
     */
    List<FriendDto> getAllFriendsForLogin(String login) throws SQLException;

    /**
     * Ставит лайк. Если запись уже есть в БД - она просто подтверждается.
     */
    void setLike(String from, String to) throws Exception;

    /**
     * Ставит жалобу, влияет на рейтинг
     */
    void setComplaint(String from, String to) throws SQLException;

    /**
     * Удаляет профиль юзера по login
     * @param login login in PathVariable
     */
    void deleteUserProfileForLogin(String login) throws SQLException;

    /**
     * Обновляет пользовательский профиль
     */
    void updateUserProfile(UserProfileDto userProfileDto) throws SQLException, JsonProcessingException;

    /**
     * Обновляет пользовательский профиль
     */
    void updateUserCoordinates(CoordinateDto coordinates, String login) throws SQLException, JsonProcessingException;

    /**
     * Проверка совпадения пароля и логина
     */
    boolean checkPassword(String login, String password) throws Exception;

    /**
     * Проставит подтвержденную почту пользователю по его токену
     */
    void confirmUserForToken(String token) throws SQLException;

    /**
     * Вернет всю историю сообщений между двумя пользователями, упорядоченную по дате в обратном порядке
     * @param user1 пользователь 1
     * @param user2 пользователь 2
     * @return список сообщений
     */
    List<MessageDto> getChatHistory(String user1, String user2) throws SQLException;

    /**
     * Сохранит сообщение, все данные о нем в DTO
     * @param messageDto сообщение
     */
    void saveChatMessage(MessageDto messageDto) throws Exception;

    /**
     * Сохраняет в БД запись о картинке, прикрепляется к пользователю
     * @param user Логин, к которому прикрепится картинка
     * @return номер картинки в БД
     */
    String saveImage(String user) throws SQLException;

    /**
     * Удалит запись о картинке в БД
     * @param user логин пользователя
     * @param id ID картинки
     * @throws AccessDeniedException Нельзя удалять чужие картинки
     */
    void deleteImage(String user, String id) throws SQLException, AccessDeniedException;

    /**
     * Отметит картинку как аватарку
     * @param imageId ID картинки
     * @param userLogin Логин пользователя
     */
    void setMainImage(String imageId, String userLogin) throws SQLException;

    /**
     * Вернет список всех картинок пользователя
     * @param user Логин пользователя
     * @return Список всех картинок одного пользователя
     */
    List<UserPhotoDto> getUserPhotos(String user) throws SQLException;

    /**
     * Поиск пользователей по фильтру, исключая текущего юзера
     * @param filter структура фильтра
     * @param login логин пользователя, который запросил сервис
     * @return Список юзеров
     */
    List<UserProfileDto> getUsersWithFilter(UserFilterDto filter, String login) throws SQLException, JsonProcessingException;

    /**
     * Проверяет, есть ли такая почта в БД
     * @param email почта
     * @return boolean
     */
    boolean checkEmailExist(String email) throws SQLException;
}
