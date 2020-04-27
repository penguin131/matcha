package com.matcha.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matcha.dto.UserProfileDto;
import com.matcha.helper.ReadHelper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;

@RestController
public class DatabaseService {

    //todo фабрику подключений и протестить производительность, добавить логирование в случае фейлов
    @Resource
    private DataSource dataSource;
    private Connection connection;
    private ObjectMapper mapper;

    @PostConstruct
    private void init() throws ClassNotFoundException, SQLException {
        HikariDataSource dSource = (HikariDataSource) dataSource;
        Class.forName(dSource.getDriverClassName());
        connection = DriverManager.getConnection(
                dSource.getJdbcUrl(),
                dSource.getUsername(),
                dSource.getPassword()
        );
        mapper = new ObjectMapper();
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
    public Boolean createUserProfile(InputStream inputData) {
        try {
            String data = ReadHelper.readJSON(inputData);
            UserProfileDto userProfileDto = mapper.readValue(data, UserProfileDto.class);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into my_db.t_user_profile (sex, sex_preferences, biography, password, login, email) VALUE (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, userProfileDto.getSex());
            preparedStatement.setInt(2, userProfileDto.getSexPreferences());
            preparedStatement.setString(3, userProfileDto.getBiography());
            preparedStatement.setString(5, userProfileDto.getLogin());
            preparedStatement.setString(4, userProfileDto.getPassword());
            preparedStatement.setString(6, userProfileDto.getEmail());
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Вернет первый профиль юзера по login
     * @param login login in PathVariable
     * @return JSON UserProfile
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "/getUserProfileForLogin",
            produces = "application/json")
    @ResponseBody
    public String getUserProfileForLogin(@RequestParam("login") String login) throws JsonProcessingException {
        UserProfileDto userProfile = new UserProfileDto();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select sex, sex_preferences, biography, password, login, email from my_db.t_user_profile where login=?");
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userProfile.setSex(rs.getInt(1));
                userProfile.setSexPreferences(rs.getInt(2));
                userProfile.setBiography(rs.getString(3));
                userProfile.setPassword(rs.getString(4));
                userProfile.setLogin(rs.getString(5));
                userProfile.setEmail(rs.getString(6));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return mapper.writeValueAsString(userProfile);
    }
}
