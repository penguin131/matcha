package com.service;

import com.dictionary.MessageType;
import com.dictionary.Sex;
import com.dto.*;
import com.exceptions.AccessDeniedException;
import com.exceptions.ValidateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.ConnectionPool;
import com.helper.Password;
import com.dto.UserPhotoDto;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseServiceSQLImpl implements DatabaseService {
    private final Logger logger = Logger.getLogger(DatabaseServiceSQLImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConnectionPool connectionFactory;

    public DatabaseServiceSQLImpl(ConnectionPool connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    /**
     * Вернет массив всех профилей. Временная шняга
     * @return List<UserProfileDto>
     */
    public List<UserProfileDto> getAllUsers() throws SQLException, JsonProcessingException {
        logger.info("getAllUsers()");
        List<UserProfileDto> userProfileList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select *," +
                    " (select id_image from \"spark-db\".t_image where user_profile_id=user_id and is_main=true limit 1) as photo " +
                    " from \"spark-db\".t_user_profile");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                UserProfileDto userProfile = new UserProfileDto(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("login"),
                        rs.getString("password"),
                        Sex.convertCodeToString(rs.getInt("sex")),
                        Sex.convertCodeToString(rs.getInt("sex_preferences")),
                        rs.getString("biography"),
                        rs.getString("email"),
                        rs.getBoolean("confirmed"),
                        rs.getString("confirmed_token"),
                        new float[2],
                        rs.getInt("photo"));
                userProfileList.add(userProfile);
            }
        } catch (SQLException ex) {
            logger.info("getAllUsers() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
        logger.info("getAllUsers() result:\n" + mapper.writeValueAsString(userProfileList));
        return userProfileList;
    }

    /**
     * Добавление нового пользовательского профиля
     */
    public void createUserProfile(BaseUserProfileDto userProfileDto, String confirmed_token) throws Exception {
        logger.info("createUserProfile()");
        if (userProfileDto == null) {
            processException(new ValidateException("userProfileDto is null!"));
        }
        assert userProfileDto != null;
        if (getUserProfileForLogin(userProfileDto.getLogin()) != null) {
            processException(new ValidateException("Login already exists!"));
        }
        if (checkEmailExist(userProfileDto.getEmail())) {
            processException(new ValidateException("Email is already in use"));
        }
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            userProfileDto.setPassword(Password.getSaltedHash(userProfileDto.getPassword()));
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark-db\".t_user_profile (login, password, email, sex, confirmed_token, confirmed) " +
                            "VALUES (?, ?, ?, ?, ?, false )");
            preparedStatement.setString(1, userProfileDto.getLogin());
            preparedStatement.setString(2, userProfileDto.getPassword());
            preparedStatement.setString(3, userProfileDto.getEmail());
            preparedStatement.setInt(4, Sex.convertStringToCode(userProfileDto.getSex()));
            preparedStatement.setString(5, confirmed_token);
            preparedStatement.execute();
            logger.info(mapper.writeValueAsString(userProfileDto));
        } catch (SQLException ex) {
            logger.info("createUserProfile() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    private boolean checkEmailExist(String email) throws SQLException {
        logger.info("checkEmailExist(), email: " + email);
        Connection connection = null;
        try  {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark-db\".t_user_profile where email=?");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            logger.info("checkEmailExist() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    /**
     * Вернет первый профиль юзера по login
     * @param login login in PathVariable
     * @return UserProfileDto
     */
    public UserProfileDto getUserProfileForLogin(String login) throws SQLException, JsonProcessingException {
        logger.info("getUserProfileForLogin() login: " + login);
        UserProfileDto userProfile = null;
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select *," +
                    " (select id_image from \"spark-db\".t_image where user_profile_id=user_id and is_main=true limit 1) as photo " +
                    " from \"spark-db\".t_user_profile where login=?");
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userProfile = new UserProfileDto(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("login"),
                        rs.getString("password"),
                        Sex.convertCodeToString(rs.getInt("sex")),
                        Sex.convertCodeToString(rs.getInt("sex_preferences")),
                        rs.getString("biography"),
                        rs.getString("email"),
                        rs.getBoolean("confirmed"),
                        rs.getString("confirmed_token"),
                        new float[]{rs.getFloat("location_1"), rs.getFloat("location_2")},
                        rs.getInt("photo"));
                logger.info("userProfile :\n" + mapper.writeValueAsString(userProfile));
            } else {
                logger.info("No user profile with login: " + login);
            }
        } catch (JsonProcessingException | SQLException ex) {
            logger.info("getUserProfileForLogin() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
        return userProfile;
    }

    /**
     * Вернет всех друзей с последним сообщением
     * @param login login
     * @return List<FriendDto>
     */
    public List<FriendDto> getAllFriendsForLogin(String login) throws SQLException {
        logger.info("getAllFriendsForLogin() login: " + login);
        List<FriendDto> friendList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "WITH cte_message AS (SELECT row_number() over (order by date desc) as nb, text, date, \"from\", \"to\"\n" +
                    "                    FROM \"spark-db\".t_message\n" +
                    "                    where \"from\"=(select user_profile_id from \"spark-db\".t_user_profile where login=?) or\n" +
                    "                             \"to\"=(select user_profile_id from \"spark-db\".t_user_profile where login=?))\n" +
                    "select t4.login, t3.text, t3.date, t3.\"from\", t3.\"to\", t1.user_profile_id, t4.user_profile_id, nb from \"spark-db\".t_user_profile t1\n" +
                    "    join \"spark-db\".t_users_unity t2 on (t1.user_profile_id=t2.user1_id or t1.user_profile_id=t2.user2_id)\n" +
                    "    join \"spark-db\".t_user_profile t4 on (t4.user_profile_id=t2.user2_id or t4.user_profile_id=t2.user1_id)\n" +
                    "    left join cte_message t3 on ((t3.\"from\"=t1.user_profile_id and t3.\"to\"=t4.user_profile_id) or (t3.\"from\"=t4.user_profile_id and t3.\"to\"=t1.user_profile_id)) and nb=1\n" +
                    "where t1.login=? and t2.confirmed=true and t1.login != t4.login");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, login);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                friendList.add(new FriendDto(rs.getString("login"), rs.getString("text"), rs.getLong("date")));
            }
            logger.info("friends count: " + friendList.size());
        } catch (SQLException ex) {
            logger.info("getAllFriendsForLogin() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
        return friendList;
    }

    /**
     * Ставит лайк. Если запись уже есть в БД - она просто подтверждается.
     */
    public void setLike(String from, String to) throws Exception {
        logger.info("setLike() from: " + from);
        if (from.equals(to))
            throw new Exception("User cannot be friends with himself");
        if (getUserProfileForLogin(to) == null)
            throw new Exception(String.format("User %s does not exists", to));
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            if (checkUserUnity(from, to)) {//если запись уже есть, тупо подтверждаю ее
                logger.info("rs.next() == true");
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "WITH cte_unity AS (\n" +
                    "    select\n" +
                    "        t1.t_users_unity_id\n" +
                    "    from \"spark-db\".t_users_unity t1\n" +
                    "             join \"spark-db\".t_user_profile t2 on (t2.user_profile_id=t1.user1_id)\n" +
                    "             join \"spark-db\".t_user_profile t3 on (t3.user_profile_id=t1.user2_id)\n" +
                    "    where t2.login=? and t3.login=?)\n" +
                    " update \"spark-db\".t_users_unity set confirmed=true " +
                    " where t_users_unity_id in (select * from cte_unity)\n");
                preparedStatement.setString(1, to);
                preparedStatement.setString(2, from);
                preparedStatement.executeUpdate();
            } else if (!checkUserUnity(to, from)) {
                logger.info("rs.next() == false");
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into \"spark-db\".t_users_unity (user1_id, user2_id) VALUES " +
                        " ((select user_profile_id from \"spark-db\".t_user_profile where login=?)," +
                        " (select user_profile_id from \"spark-db\".t_user_profile where login=?))");
                preparedStatement.setString(1, from);
                preparedStatement.setString(2, to);
                preparedStatement.execute();
            }
            logger.info("setLike OK");
        } catch (SQLException ex) {
            logger.info("setLike() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    private boolean checkUserUnity(String login1, String login2) throws SQLException {
        logger.info(String.format("checkUserUnity: %s, %s", login1, login2));
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark-db\".t_users_unity t1 " +
                    " join \"spark-db\".t_user_profile t2 on (t2.user_profile_id=t1.user1_id)" +
                    " join \"spark-db\".t_user_profile t3 on (t3.user_profile_id=t1.user2_id)" +
                    " where t2.login=? and t3.login=?");
            preparedStatement.setString(1, login1);
            preparedStatement.setString(2, login2);
            ResultSet rs = preparedStatement.executeQuery();
            boolean result = rs.next();
            logger.info("result: " + result);
            return result;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    /**
     * Удаляет профиль юзера по login
     * @param login login in PathVariable
     */
    public void deleteUserProfileForLogin(String login) throws SQLException {
        logger.info("deleteUserProfileForLogin() login: " + login);
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from \"spark-db\".t_user_profile where login=?");
            preparedStatement.setString(1, login);
            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            logger.info("deleteUserProfileForLogin() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    /**
     * Обновляет пользовательский профиль
     */
    public void updateUserProfile(UserProfileDto userProfileDto) throws SQLException, JsonProcessingException {
        logger.info("updateUserProfile() :\n" + mapper.writeValueAsString(userProfileDto));
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark-db\".t_user_profile set first_name=?, last_name=?, sex_preferences=?, biography=? where login=?");
            preparedStatement.setString(1, userProfileDto.getFirstName());
            preparedStatement.setString(2, userProfileDto.getLastName());
            preparedStatement.setInt(3, Sex.convertStringToCode(userProfileDto.getSexPreferences()));
            preparedStatement.setString(4, userProfileDto.getBiography());
            preparedStatement.setString(5, userProfileDto.getLogin());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.info("updateUserProfile() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    /**
     * Проверка совпадения пароля и логина
     */
    public boolean checkPassword(String login, String password) throws Exception {
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
    public void confirmUserForToken(String token) throws SQLException {
        logger.info("confirmUserForToken() token: " + token);
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark-db\".t_user_profile set confirmed=true where confirmed_token=?");
            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.info("confirmUserForToken() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    public List<MessageDto> getChatHistory(String user1, String user2) throws SQLException {
        logger.info(String.format("getChatHistory() user1: %s, user2: %s", user1, user2));
        List<MessageDto> messages = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select t1.text, t1.date, t2.login as login1, t3.login as login2 from \"spark-db\".t_message t1\n" +
                    " join \"spark-db\".t_user_profile t2 on (t1.\"from\"=t2.user_profile_id)\n" +
                    " join \"spark-db\".t_user_profile t3 on (t1.\"to\"=t3.user_profile_id)\n" +
                    " where t2.login=? and t3.login=? or t2.login=? and t3.login=?\n" +
                    " order by t1.date desc");
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
                        rs.getLong("date")));
            }
        } catch (SQLException ex) {
            logger.info("getChatHistory() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
        return messages;
    }

    public void saveChatMessage(MessageDto messageDto) throws Exception {
        logger.info("saveChatMessage()");
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark-db\".t_message (text, date, \"from\", \"to\") VALUES\n" +
                    "(?\n" +
                    " ,?\n" +
                    " ,(select user_profile_id from \"spark-db\".t_user_profile where login=?)\n" +
                    " ,(select user_profile_id from \"spark-db\".t_user_profile where login=?))");
            preparedStatement.setString(1, messageDto.getMsgText());
            preparedStatement.setLong(2, messageDto.getDate());
            preparedStatement.setString(3, messageDto.getFrom());
            preparedStatement.setString(4, messageDto.getTo());
            preparedStatement.execute();
            logger.info(mapper.writeValueAsString(messageDto));
        } catch (SQLException ex) {
            logger.info("saveChatMessage() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    public String saveImage(String user) throws SQLException {
        logger.info(String.format("saveImage(%s)", user));
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark-db\".t_image (user_id) values" +
                    " ((select user_profile_id from \"spark-db\".t_user_profile where login=? limit 1)) returning id_image");
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("id_image");
        } catch (SQLException ex) {
            logger.info("saveImage() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    public void deleteImage(String user, String id) throws SQLException, AccessDeniedException {
        logger.info(String.format("deleteImage(%s, %s)", id, user));
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select (select login from \"spark-db\".t_user_profile where user_profile_id=user_id limit 1) as user" +
                    " from \"spark-db\".t_image where id_image=?");
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (user.equals(resultSet.getString("id_image")))
                throw new AccessDeniedException("this photo does not belong to the current user!");
            deleteImage(id);
        } catch (SQLException ex) {
            logger.info("deleteImage() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    public void deleteImage(String id) throws SQLException {
        logger.info(String.format("deleteImage(%s)", id));
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from \"spark-db\".t_image where id_image=?");
            preparedStatement.setInt(1, Integer.parseInt(id));
            preparedStatement.execute();
        } catch (SQLException ex) {
            logger.info("deleteImage() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    public void setMainImage(String imageId, String userLogin) throws SQLException {
        logger.info(String.format("setMainImage(%s)", imageId));
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark-db\".t_image set is_main=false " +
                    " where user_id=(select user_profile_id from \"spark-db\".t_user_profile where login=? limit 1)");
            preparedStatement.setString(1, userLogin);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("update \"spark-db\".t_image set is_main=true where id_image=?");
            preparedStatement.setInt(1, Integer.parseInt(imageId));
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.info("setMainImage() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
    }

    public List<UserPhotoDto> getUserPhotos(String user) throws SQLException {
        logger.info(String.format("getUserPhotos(%s)", user));
        List<UserPhotoDto> photos = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark-db\".t_image " +
                    " where user_id=(select user_profile_id from \"spark-db\".t_user_profile where login=? limit 1)");
            preparedStatement.setString(1, user);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                photos.add(new UserPhotoDto(rs.getInt("id_image"), rs.getBoolean("is_main")));
            }
        } catch (SQLException ex) {
            logger.info("setMainImage() exception:\n" + ex.getMessage());
            throw ex;
        } finally {
            connectionFactory.releaseConnection(connection);
        }
        return photos;
    }

    private void processException(Exception ex) throws Exception {
        logger.info(ex.getMessage());
        throw ex;
    }
}
