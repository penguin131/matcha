package com.service;

import com.dto.BaseUserProfileDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.DatabaseConfig;
import com.helper.Password;
import com.security.SecurityHelper;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static DatabaseConfig.DatabaseProperties props = null;
    private final static Logger logger = Logger.getLogger(DatabaseService.class);
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            props = DatabaseConfig.getDatabaseProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Вернет массив всех профилей. Временная шняга
     * @return List<UserProfileDto>
     */
    public static List<UserProfileDto> getAllUsers() throws SQLException, JsonProcessingException {
        logger.info("getAllUsers()");
        List<UserProfileDto> userProfileList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark-db\".t_user_profile");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                UserProfileDto userProfile = new UserProfileDto(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getInt("sex"),
                        rs.getInt("sex_preferences"),
                        rs.getString("biography"),
                        rs.getString("email"),
                        rs.getBoolean("confirmed"));
                userProfileList.add(userProfile);
            }
        } catch (SQLException ex) {
            logger.info("getAllUsers() exception:\n" + ex.getMessage());
            throw ex;
        }
        logger.info("getAllUsers() result:\n" + mapper.writeValueAsString(userProfileList));
        return userProfileList;
    }

    /**
     * Добавление нового пользовательского профиля
     */
    public static void createUserProfile(BaseUserProfileDto userProfileDto) throws Exception {
        logger.info("createUserProfile()");
        if (userProfileDto == null) {
            processException(new Exception("userProfileDto is null!"));
        }
        assert userProfileDto != null;
        if (getUserProfileForLogin(userProfileDto.getLogin()) != null) {
            processException(new Exception("Login already exists!"));
        }
        if (checkEmailExist(userProfileDto.getEmail())) {
            processException(new Exception("Email is already in use"));
        }
        try {
            Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
            userProfileDto.setPassword(Password.getSaltedHash(userProfileDto.getPassword()));
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark-db\".t_user_profile (login, password, email, sex, confirmed_token, confirmed) " +
                            "VALUES (?, ?, ?, ?, ?, false )");
            preparedStatement.setString(1, userProfileDto.getLogin());
            preparedStatement.setString(2, userProfileDto.getPassword());
            preparedStatement.setString(3, userProfileDto.getEmail());
            preparedStatement.setInt(4, userProfileDto.getSex());
            preparedStatement.setString(5, SecurityHelper.generateHash());
            preparedStatement.execute();
            logger.info(mapper.writeValueAsString(userProfileDto));
        } catch (SQLException ex) {
            logger.info("createUserProfile() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    private static boolean checkEmailExist(String email) throws SQLException {
        try {
            logger.info("checkEmailExist(), email: " + email);
            Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark-db\".t_user_profile where email=?");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            logger.info("checkEmailExist() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Вернет первый профиль юзера по login
     * @param login login in PathVariable
     * @return JSON UserProfileDto
     */
    public static UserProfileDto getUserProfileForLogin(String login) throws SQLException, JsonProcessingException {
        logger.info("getUserProfileForLogin() login: " + login);
        Connection connection;
        UserProfileDto userProfile = null;
        try {
            connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark-db\".t_user_profile where login=?");
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userProfile = new UserProfileDto(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getInt("sex"),
                        rs.getInt("sex_preferences"),
                        rs.getString("biography"),
                        rs.getString("email"),
                        rs.getBoolean("confirmed"));
                logger.info("userProfile :\n" + mapper.writeValueAsString(userProfile));
            } else {
                logger.info("No user profile with login: " + login);
            }
        } catch (JsonProcessingException | SQLException ex) {
            logger.info("getUserProfileForLogin() exception:\n" + ex.getMessage());
            throw ex;
        }
        return userProfile;
    }

    /**
     * Обновляет пользовательский профиль
     */
    public static void updateUserProfile(UserProfileDto userProfileDto) throws SQLException, JsonProcessingException {
        try {
            logger.info("updateUserProfile() :\n" + mapper.writeValueAsString(userProfileDto));
            Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark-db\".t_user_profile set first_name=?, last_name=?, sex_preferences=?, biography=? where login=?");
            preparedStatement.setString(1, userProfileDto.getFirstName());
            preparedStatement.setString(2, userProfileDto.getLastName());
            preparedStatement.setInt(3, userProfileDto.getSexPreferences());
            preparedStatement.setString(4, userProfileDto.getBiography());
            preparedStatement.setString(5, userProfileDto.getLogin());
            preparedStatement.execute();
        } catch (JsonProcessingException | SQLException ex) {
            logger.info("updateUserProfile() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Проверка совпадения пароля и логина
     */
    public static boolean checkPassword(String login, String password) throws Exception {
        logger.info(String.format("checkPassword() login: %s, password: %s", login, password));
        UserProfileDto user = getUserProfileForLogin(login);
        if (user == null) {
            throw new Exception("Invalid login + password");
        }
        return Password.check(password, user.getPassword());
    }

    /**
     * Проставит подтвержденную почту пользователю по его токену
     */
    public static void confirmUserForToken(String token) throws SQLException {
        logger.info("confirmUserForToken() token: " + token);
        Connection connection;
        try {
            connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark-db\".t_user_profile set confirm=true where token=?");
            preparedStatement.setString(1, token);
            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            logger.info("confirmUserForToken() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    private static void processException(Exception ex) throws Exception {
        logger.info(ex.getMessage());
        throw ex;
    }
}
