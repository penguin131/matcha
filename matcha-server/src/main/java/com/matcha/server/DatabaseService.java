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
import java.sql.*;

@RestController
public class DatabaseService {

    //todo фабрику подключений и протестить производительность
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
    public String addUserProfile(InputStream inputData) {
        try {
            JSONObject jsonObject = new JSONObject(ReadHelper.readJSON(inputData));
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into my_db.t_user_profile (sex, sex_preferences, biography) VALUE (?, ?, ?)");
            preparedStatement.setString(1, jsonObject.getString("sex"));
            preparedStatement.setString(2, jsonObject.getString("sex_preferences"));
            preparedStatement.setString(3, jsonObject.getString("biography"));
            preparedStatement.execute();
            return "ok";
        } catch (Exception ex) {
            return "err:" + ex.getMessage();
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
}
