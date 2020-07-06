package com.service;

import com.dto.BaseUserProfileDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.DatabaseConfig;
import com.helper.Password;
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
    public static List<UserProfileDto> getAllUsers() throws Exception {
        List<UserProfileDto> userProfileList = new ArrayList<>();
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
        return userProfileList;
    }

    /**
     * Добавление нового пользовательского профиля
     */
    public static void createUserProfile(BaseUserProfileDto userProfileDto) throws Exception {
        if (getUserProfileForLogin(userProfileDto.getLogin()) != null)
            throw new Exception("Login already exists!");
        if (checkEmailExist(userProfileDto.getEmail()))
            throw new Exception("Email is already in use");
        Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
        userProfileDto.setPassword(Password.getSaltedHash(userProfileDto.getPassword()));
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into \"spark-db\".t_user_profile (login, password, email, sex, confirmed) " +
                        "VALUES (?, ?, ?, ?, false)");
        preparedStatement.setString(1, userProfileDto.getLogin());
        preparedStatement.setString(2, userProfileDto.getPassword());
        preparedStatement.setString(3, userProfileDto.getEmail());
        preparedStatement.setInt(4, userProfileDto.getSex());
        preparedStatement.execute();
    }

    private static boolean checkEmailExist(String email) throws Exception {
        Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from \"spark-db\".t_user_profile where email=?");
        preparedStatement.setString(1, email);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    /**
     * Вернет первый профиль юзера по login
     * @param login login in PathVariable
     * @return JSON UserProfileDto
     */
    public static UserProfileDto getUserProfileForLogin(String login) throws Exception {
        logger.info("getUserProfileForLogin() login: " + login);
        UserProfileDto userProfile;
        Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
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
        } else {
            logger.info("No user profile with login: " + login);
            return null;
        }
        logger.info("userProfile :\n" + mapper.writeValueAsString(userProfile));
        return userProfile;
    }

    /**
     * Обновляет пользовательский профиль
     */
    public static void updateUserProfile(UserProfileDto userProfileDto) throws Exception {
        Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update \"spark-db\".t_user_profile set first_name=?, last_name=?, sex_preferences=?, biography=? where login=?");
        preparedStatement.setString(1, userProfileDto.getFirstName());
        preparedStatement.setString(2, userProfileDto.getLastName());
        preparedStatement.setInt(3, userProfileDto.getSexPreferences());
        preparedStatement.setString(4, userProfileDto.getBiography());
        preparedStatement.setString(5, userProfileDto.getLogin());
        preparedStatement.execute();
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
}
