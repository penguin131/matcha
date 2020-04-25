package com.matcha.server;

import com.matcha.helper.ReadHelper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Arrays;

@RestController
public class DatabaseService {

    //todo фабрику подключений и протестить производительность, добавить логирование в случае фейлов
    @Resource
    private DataSource dataSource;
    private Connection connection;

    @PostConstruct
    private void init() throws ClassNotFoundException, SQLException {
        HikariDataSource dSource = (HikariDataSource) dataSource;
        Class.forName(dSource.getDriverClassName());
        connection = DriverManager.getConnection(
                dSource.getJdbcUrl(),
                dSource.getUsername(),
                dSource.getPassword()
        );
    }

    /**
     * Добавление нового пользовательского профиля
     * @param inputData - JSON представление информации о новом пользовательском профиле
     * @return status in String
     */
    @RequestMapping(method = RequestMethod.POST,
                    value = "/createUserProfile",
                    consumes = "application/json")
    @ResponseBody
    public Boolean addUserProfile(InputStream inputData) {
        try {
            JSONObject jsonObject = new JSONObject(ReadHelper.readJSON(inputData));
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into my_db.t_user_profile (sex, sex_preferences, biography, password, login) VALUE (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, jsonObject.getString("sex"));
            preparedStatement.setString(2, jsonObject.getString("sex_preferences"));
            preparedStatement.setString(3, jsonObject.getString("biography"));
            preparedStatement.setString(5, jsonObject.getString("login"));
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(jsonObject.getString("password").getBytes(StandardCharsets.UTF_8));
            preparedStatement.setString(4, Arrays.toString(hash));
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Вернет профиль юзера по ID
     * @param inputData user_profile_id
     * @return JSON UserProfile
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "/getUserProfileForId",
            consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public String getUserProfileForId(InputStream inputData) {
        try {
            JSONObject jsonObject = new JSONObject(ReadHelper.readJSON(inputData));
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from my_db.t_user_profile where user_profile_id=?");
            preparedStatement.setString(1, jsonObject.getString("user_profile_id"));
            ResultSet rs = preparedStatement.executeQuery();
            JSONObject userProfile = new JSONObject();
            if (rs.next()) {
                userProfile.put("sex", rs.getInt(2));
                userProfile.put("sex_preferences", rs.getInt(3));
                userProfile.put("biography", rs.getString(4));
            }
            return userProfile.toString();
        } catch (Exception ex) {
            return "err:" + ex.getMessage();
        }
    }

    /**
     * Проверит, правильный ли пароль у юзера
     * @param inputData login, password in JSON
     * @return true/false
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "/checkUserPassword",
            consumes = "application/json")
    @ResponseBody
    public Boolean checkUserPassword(InputStream inputData) {
        try {
            JSONObject jsonObject = new JSONObject(ReadHelper.readJSON(inputData));
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select password from my_db.t_user_profile where login=?");
            preparedStatement.setString(1, jsonObject.getString("login"));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(jsonObject.getString("password").getBytes(StandardCharsets.UTF_8));
                return rs.getString(1).equals(Arrays.toString(hash));
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

}
