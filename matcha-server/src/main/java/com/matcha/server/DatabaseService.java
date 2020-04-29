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
import java.util.ArrayList;
import java.util.List;

@RestController
public class DatabaseService {

    //todo добавить логирование в случае фейлов
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
     * Вернет массив профилей. Временная шняга
     * @return JSON UserProfile[]
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "/getAllUsers",
            produces = "application/json")
    @ResponseBody
    public String getAllUsers() throws JsonProcessingException {
        List<UserProfileDto> userProfileList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from my_db.t_user_profile");
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
                        rs.getInt("confirmed"));
                userProfileList.add(userProfile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return mapper.writeValueAsString(userProfileList);
    }

    /**
     * Добавление нового пользовательского профиля
     * @param inputData - JSON представление информации о новом пользовательском профиле
     * @return status
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
                    "insert into my_db.t_user_profile (first_name, last_name, login, password, email, sex, confirmed) VALUE (?, ?, ?, ?, ?, ?, 0)");
            preparedStatement.setString(1, userProfileDto.getFirstName());
            preparedStatement.setString(2, userProfileDto.getLastName());
            preparedStatement.setString(3, userProfileDto.getLogin());
            preparedStatement.setString(4, userProfileDto.getPassword());
            preparedStatement.setString(5, userProfileDto.getEmail());
            preparedStatement.setInt(6, userProfileDto.getSex());
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
        UserProfileDto userProfile;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from my_db.t_user_profile where login=?");
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
                        rs.getInt("confirmed"));
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return mapper.writeValueAsString(userProfile);
    }

    /**
     * Обновляет пользовательский профиль
     * @param inputData JSON с новыми данными
     * @return статус
     */
    @RequestMapping(method = RequestMethod.POST,
                    value = "/updateUserProfile",
                    consumes = "application/json")
    @ResponseBody
    public Boolean updateUserProfile(InputStream inputData) {
        try {
            String data = ReadHelper.readJSON(inputData);
            UserProfileDto userProfileDto = mapper.readValue(data, UserProfileDto.class);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update my_db.t_user_profile set first_name=?, last_name=?, sex_preferences=?, biography=?, email=? where login=?");
            preparedStatement.setString(1, userProfileDto.getFirstName());
            preparedStatement.setString(2, userProfileDto.getLastName());
            preparedStatement.setInt(3, userProfileDto.getSexPreferences());
            preparedStatement.setString(4, userProfileDto.getBiography());
            preparedStatement.setString(5, userProfileDto.getEmail());
            preparedStatement.setString(6, userProfileDto.getLogin());
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Добавляет связь двух пользователей. Так как это просто чатики, подтверждать дружбу не нужно.
     * @param user1, user2 - ID двух пользователей, которые связываются друг с другом.
     * @param isFriend: 0-в черном списке.
     * @return статус
     */
    @RequestMapping(method = RequestMethod.GET,
                    value = "/createRelation")
    @ResponseBody
    public Boolean createRelation(@RequestParam("user_1") int user1,
                                  @RequestParam("user_2") int user2,
                                  @RequestParam("is_friend") int isFriend) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into my_db.t_relations user_1, user_2, is_friend values ?, ?, ?");
            preparedStatement.setInt(1, user1);
            preparedStatement.setInt(2, user2);
            preparedStatement.setInt(3, isFriend);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Удаляет связь двух пользователей.
     * @param user1, user2 - ID двух пользователей, которые связываются друг с другом.
     * @return статус
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "/dropRelation")
    @ResponseBody
    public Boolean dropRelation(@RequestParam("user_1") int user1,
                                @RequestParam("user_2") int user2) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from my_db.t_relations where user_1=? and user_2=?");
            preparedStatement.setInt(1, user1);
            preparedStatement.setInt(2, user2);
            preparedStatement.execute();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
