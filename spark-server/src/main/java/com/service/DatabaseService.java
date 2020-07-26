package com.service;

import com.dictionary.MessageType;
import com.dto.BaseUserProfileDto;
import com.dto.FriendDto;
import com.dto.MessageDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public static List<UserProfileDto> getAllUsers() throws SQLException, JsonProcessingException {
        logger.info("getAllUsers()");
        List<UserProfileDto> userProfileList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())) {
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
                        rs.getBoolean("confirmed"),
                        rs.getString("confirmed_token"));
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
    public static void createUserProfile(BaseUserProfileDto userProfileDto, String confirmed_token) throws Exception {
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
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())) {
            userProfileDto.setPassword(Password.getSaltedHash(userProfileDto.getPassword()));
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark-db\".t_user_profile (login, password, email, sex, confirmed_token, confirmed) " +
                            "VALUES (?, ?, ?, ?, ?, false )");
            preparedStatement.setString(1, userProfileDto.getLogin());
            preparedStatement.setString(2, userProfileDto.getPassword());
            preparedStatement.setString(3, userProfileDto.getEmail());
            preparedStatement.setInt(4, userProfileDto.getSex());
            preparedStatement.setString(5, confirmed_token);
            preparedStatement.execute();
            logger.info(mapper.writeValueAsString(userProfileDto));
        } catch (SQLException ex) {
            logger.info("createUserProfile() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    private static boolean checkEmailExist(String email) throws SQLException {
        logger.info("checkEmailExist(), email: " + email);
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())) {
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
     * @return UserProfileDto
     */
    public static UserProfileDto getUserProfileForLogin(String login) throws SQLException, JsonProcessingException {
        logger.info("getUserProfileForLogin() login: " + login);
        UserProfileDto userProfile = null;
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())) {
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
                        rs.getBoolean("confirmed"),
                        rs.getString("confirmed_token"));
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
     * Вернет всех друзей с последним сообщением
     * @param login login
     * @return List<FriendDto>
     */
    public static List<FriendDto> getAllFriendsForLogin(String login) throws SQLException {
        logger.info("getAllFriendsForLogin() login: " + login);
        List<FriendDto> friendList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())){
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "WITH cte_message AS (\n" +
                            "    SELECT\n" +
                            "        row_number() over () as \"nb\",\n" +
                            "        text,\n" +
                            "        date,\n" +
                            "        user_unity\n" +
                            "    FROM\n" +
                            "        \"spark-db\".t_message\n" +
                            "    where\n" +
                            "        t_message.user_unity in (select t_users_unity_id from \"spark-db\".t_users_unity)\n" +
                            "    order by date desc\n" +
                            ")\n" +
                            "select t4.login, t3.text, t3.date from \"spark-db\".t_user_profile t1\n" +
                            "    join \"spark-db\".t_users_unity t2 on (t1.user_profile_id=t2.user1_id or t1.user_profile_id=t2.user2_id)\n" +
                            "    join \"spark-db\".t_user_profile t4 on (t4.user_profile_id=t2.user2_id) or t4.user_profile_id=t2.user1_id\n" +
                            "    left join cte_message t3 on (t3.user_unity=t2.t_users_unity_id and t3.nb=1)\n" +
                            "    where t1.login=? and t2.confirmed=true and t1.login != t4.login");
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                friendList.add(new FriendDto(rs.getString("login"), rs.getString("text"), rs.getDate("date")));
            }
            logger.info("friends count: " + friendList.size());
        } catch (SQLException ex) {
            logger.info("getAllFriendsForLogin() exception:\n" + ex.getMessage());
            throw ex;
        }
        return friendList;
    }

    /**
     * Ставит лайк. Если запись уже есть в БД - она просто подтверждается.
     */
    public static void setLike(String from, String to) throws Exception {
        logger.info("setLike() from: " + from);
        if (from.equals(to))
            throw new Exception("User cannot be friends with himself");
        if (getUserProfileForLogin(to) == null)
            throw new Exception(String.format("User %s does not exists", to));
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())) {
            if (checkUserUnity(from, to)) {//если запись уже есть, тупо подтверждаю ее
                logger.info("rs.next() == true");
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "WITH cte_unity AS (\n" +
                            "    select\n" +
                            "        t1.t_users_unity_id\n" +
                            "    from \"spark-db\".t_users_unity t1\n" +
                            "             join \"spark-db\".t_user_profile t2 on (t2.user_profile_id=t1.user1_id)\n" +
                            "             join \"spark-db\".t_user_profile t3 on (t3.user_profile_id=t1.user2_id)\n" +
                            "    where t2.login=? and t3.login=?\n" +
                            ")\n" +
                            "update \"spark-db\".t_users_unity set confirmed=true where t_users_unity_id in (select * from cte_unity)\n");
                preparedStatement.setString(1, to);
                preparedStatement.setString(2, from);
                preparedStatement.executeUpdate();
            } else if (!checkUserUnity(to, from)) {
                logger.info("rs.next() == false");
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into \"spark-db\".t_users_unity (user1_id, user2_id) VALUES " +
                                "((select user_profile_id from \"spark-db\".t_user_profile where login=?)," +
                                " (select user_profile_id from \"spark-db\".t_user_profile where login=?))");
                preparedStatement.setString(1, from);
                preparedStatement.setString(2, to);
                preparedStatement.execute();
            }
            logger.info("setLike OK");
        } catch (SQLException ex) {
            logger.info("setLike() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    private static boolean checkUserUnity(String login1, String login2) throws SQLException {
        logger.info(String.format("checkUserUnity: %s, %s", login1, login2));
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark-db\".t_users_unity t1 " +
                            "join \"spark-db\".t_user_profile t2 on (t2.user_profile_id=t1.user1_id)" +
                            "join \"spark-db\".t_user_profile t3 on (t3.user_profile_id=t1.user2_id)" +
                            "where t2.login=? and t3.login=?");
            preparedStatement.setString(1, login1);
            preparedStatement.setString(2, login2);
            ResultSet rs = preparedStatement.executeQuery();
            boolean result = rs.next();
            logger.info("result: " + result);
            return result;
        }
    }

    /**
     * Удаляет профиль юзера по login
     * @param login login in PathVariable
     */
    public static void deleteUserProfileForLogin(String login) throws SQLException {
        logger.info("deleteUserProfileForLogin() login: " + login);
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())){
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from \"spark-db\".t_user_profile where login=?");
            preparedStatement.setString(1, login);
            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            logger.info("deleteUserProfileForLogin() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Обновляет пользовательский профиль
     */
    public static void updateUserProfile(UserProfileDto userProfileDto) throws SQLException, JsonProcessingException {
        logger.info("updateUserProfile() :\n" + mapper.writeValueAsString(userProfileDto));
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())){
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark-db\".t_user_profile set first_name=?, last_name=?, sex_preferences=?, biography=? where login=?");
            preparedStatement.setString(1, userProfileDto.getFirstName());
            preparedStatement.setString(2, userProfileDto.getLastName());
            preparedStatement.setInt(3, userProfileDto.getSexPreferences());
            preparedStatement.setString(4, userProfileDto.getBiography());
            preparedStatement.setString(5, userProfileDto.getLogin());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
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
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());){
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark-db\".t_user_profile set confirmed=true where confirmed_token=?");
            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.info("confirmUserForToken() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    public static List<MessageDto> getChatHistory(String user1, String user2) throws SQLException {
        logger.info(String.format("getChatHistory() user1: %s, user2: %s", user1, user2));
        List<MessageDto> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties());){
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select t1.text, t1.date, t3.login as login1, t4.login as login2 from \"spark-db\".t_message t1\n" +
                            "join \"spark-db\".t_users_unity t2 on (t2.t_users_unity_id=t1.user_unity)\n" +
                            "join \"spark-db\".t_user_profile t3 on (t3.user_profile_id=t2.user1_id)\n" +
                            "join \"spark-db\".t_user_profile t4 on (t4.user_profile_id=t2.user2_id)\n" +
                            "where t3.login=? and t4.login=? or t3.login=? and t4.login=?\n" +
                            "order by t1.date desc");
            preparedStatement.setString(1, user1);
            preparedStatement.setString(2, user2);
            preparedStatement.setString(3, user2);
            preparedStatement.setString(4, user1);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                messages.add(new MessageDto(MessageType.CHAT_MESSAGE,
                        rs.getString("text"),
                        rs.getString("login1"),
                        rs.getString("login2"),
                        rs.getDate("date").getTime()));
            }
        } catch (SQLException ex) {
            logger.info("getChatHistory() exception:\n" + ex.getMessage());
            throw ex;
        }
        return messages;
    }

    public static void saveChatMessage(MessageDto messageDto) throws Exception {
        logger.info("saveChatMessage()");
        try (Connection connection = DriverManager.getConnection(props.getUrl(), props.getProperties())) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark-db\".t_message (text, user_unity, date) values\n" +
                            "(?,\n" +
                            " (select t2.t_users_unity_id from \"spark-db\".t_users_unity t2\n" +
                            "       join \"spark-db\".t_user_profile t3 on (t3.user_profile_id=t2.user1_id)\n" +
                            "       join \"spark-db\".t_user_profile t4 on (t4.user_profile_id=t2.user2_id)\n" +
                            "     where t3.login=? and t4.login=? or t3.login=? and t4.login=?" +
                            "limit 1),\n" +
                            " ?);");
            preparedStatement.setString(1, messageDto.getMsgText());
            preparedStatement.setString(2, messageDto.getFrom());
            preparedStatement.setString(3, messageDto.getTo());
            preparedStatement.setString(4, messageDto.getTo());
            preparedStatement.setString(5, messageDto.getFrom());
            preparedStatement.setDate(6, new Date(messageDto.getDate()));
            preparedStatement.execute();
            logger.info(mapper.writeValueAsString(messageDto));
        } catch (SQLException ex) {
            logger.info("saveChatMessage() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    private static void processException(Exception ex) throws Exception {
        logger.info(ex.getMessage());
        throw ex;
    }
}
