package com.service;

import com.dictionary.MessageType;
import com.dictionary.Sex;
import com.dto.*;
import com.exceptions.AccessDeniedException;
import com.exceptions.ValidateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.Password;
import com.helper.SQLRequestGenerationHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.helper.SQLRequestGenerationHelper.addValuesToPreparedStatement;
import static com.helper.SQLRequestGenerationHelper.generateUserSearchRequest;

public class DatabaseServiceSQLImpl implements DatabaseService {
    private final Logger logger = Logger.getLogger(DatabaseServiceSQLImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private Connection connection;

    public DatabaseServiceSQLImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<UserProfileDto> getAllUsers(String login) throws SQLException, JsonProcessingException {
        return getUsersWithFilter(null, login);
    }

    @Override
    public void createUserProfile(BaseUserProfileDto userProfileDto, String confirmedToken) throws JsonProcessingException, SQLException {
        logger.info(String.format("createUserProfile(%s, %s)", mapper.writeValueAsString(userProfileDto), confirmedToken));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark_db\".t_user_profile (login, password, email, sex, confirmed_token, confirmed) " +
                            "VALUES (?, ?, ?, ?, ?, false )");
            preparedStatement.setString(1, userProfileDto.getLogin());
            preparedStatement.setString(2, userProfileDto.getPassword());
            preparedStatement.setString(3, userProfileDto.getEmail());
            preparedStatement.setInt(4, Sex.convertStringToCode(userProfileDto.getSex()));
            preparedStatement.setString(5, confirmedToken);
            preparedStatement.execute();
            logger.info(mapper.writeValueAsString(userProfileDto));
        } catch (SQLException | JsonProcessingException ex) {
            logger.info("createUserProfile() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean checkEmailExist(String email) throws SQLException {
        logger.info("checkEmailExist(), email: " + email);
        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark_db\".t_user_profile where email=?");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            boolean result = rs.next();
            logger.info("checkEmailExist() result: " + result);
            return result;
        } catch (SQLException ex) {
            logger.info("checkEmailExist() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public UserProfileDto getUserProfileForLogin(String login) throws SQLException, JsonProcessingException {
        logger.info("getUserProfileForLogin() login: " + login);
        UserProfileDto userProfile = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select *" +
                    " ,(select id_image from \"spark_db\".t_image where user_profile_id=user_id and is_main=true limit 1) as photo " +
                    " ,(select count(*) from spark_db.t_users_unity where user_profile_id=user1_id or user_profile_id=user2_id and t_users_unity.confirmed=true) as has_like" +
                    " ,(select count(*) from spark_db.t_complaint where user_profile_id=from_user) as has_dislike" +
                    " from \"spark_db\".t_user_profile where login=?");
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                userProfile = UserProfileDto.getInstance(rs);
                logger.info("userProfile :\n" + mapper.writeValueAsString(userProfile));
            } else {
                logger.info("No user profile with login: " + login);
            }
        } catch (JsonProcessingException | SQLException ex) {
            ex.printStackTrace();
            logger.info("getUserProfileForLogin() exception:\n" + ex.getMessage());
            throw ex;
        }
        return userProfile;
    }

    private String getUserPassword(String login) throws SQLException {
        logger.info("getUserPassword() login: " + login);
        PreparedStatement preparedStatement = connection.prepareStatement("select password from \"spark_db\".t_user_profile where login=?");
        preparedStatement.setString(1, login);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getString("password");
        }
        return null;
    }

    @Override
    public List<FriendDto> getAllFriendsForLogin(String login) throws SQLException {
        logger.info("getAllFriendsForLogin() login: " + login);
        List<FriendDto> friendList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "WITH cte_message AS (SELECT row_number() over (order by date desc) as nb, text, date, \"from\", \"to\"\n" +
                    "                    FROM \"spark_db\".t_message\n" +
                    "                    where \"from\"=(select user_profile_id from \"spark_db\".t_user_profile where login=?) or\n" +
                    "                             \"to\"=(select user_profile_id from \"spark_db\".t_user_profile where login=?))\n" +
                    "select t4.login, t3.text, t3.date, t3.\"from\", t3.\"to\", t1.user_profile_id, t4.user_profile_id, nb from \"spark_db\".t_user_profile t1\n" +
                    "    join \"spark_db\".t_users_unity t2 on (t1.user_profile_id=t2.user1_id or t1.user_profile_id=t2.user2_id)\n" +
                    "    join \"spark_db\".t_user_profile t4 on (t4.user_profile_id=t2.user2_id or t4.user_profile_id=t2.user1_id)\n" +
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
        }
        return friendList;
    }

    @Override
    public void setLike(String from, String to) throws ValidateException, SQLException, JsonProcessingException {
        logger.info("setLike() from: " + from);
        if (StringUtils.equals(from, to))
            throw new ValidateException("User cannot be friends with himself");
        if (getUserProfileForLogin(to) == null)
            throw new ValidateException(String.format("User %s does not exists", to));
        try {
            CallableStatement preparedStatement = connection.prepareCall("{CALL set_like(?,?)}");
            preparedStatement.setString(1, from);
            preparedStatement.setString(2, to);
            preparedStatement.execute();
            logger.info("complaint created");
        } catch (SQLException ex) {
            logger.info("setLike() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void setComplaint(String from, String to) throws SQLException {
        logger.info(String.format("setComplaint(%s, %s)", from, to));
        try {
            CallableStatement preparedStatement = connection.prepareCall("{CALL set_complaint(?,?)}");
            preparedStatement.setString(1, from);
            preparedStatement.setString(2, to);
            preparedStatement.execute();
            logger.info("complaint created");
        } catch (SQLException ex) {
            logger.info("setComplaint() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void deleteUserProfileForLogin(String login) throws SQLException {
        logger.info("deleteUserProfileForLogin() login: " + login);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from \"spark_db\".t_user_profile where login=?");
            preparedStatement.setString(1, login);
            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            logger.info("deleteUserProfileForLogin() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override//todo asd
    public void updateUserProfile(InnerProfileDto userProfileDto, String login) throws SQLException, JsonProcessingException, IllegalAccessException {
        logger.info("updateUserProfile(): " + mapper.writeValueAsString(userProfileDto));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    SQLRequestGenerationHelper.generateUpdateUserRequest(userProfileDto));
            SQLRequestGenerationHelper.addValuesToPreparedStatement(preparedStatement, userProfileDto, login);
            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalAccessException ex) {
            logger.info("updateUserProfile() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updateUserCoordinates(CoordinateDto coordinates, String login) throws JsonProcessingException, SQLException {
        logger.info(String.format("updateUserCoordinates(%s, %s): ", mapper.writeValueAsString(coordinates), login));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark_db\".t_user_profile set latitude=?, longitude=? where login=?");
            preparedStatement.setFloat(1, coordinates.getLatitude());
            preparedStatement.setFloat(2, coordinates.getLongitude());
            preparedStatement.setString(3, login);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.info("updateUserCoordinates() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public boolean checkPassword(String login, String password) throws Exception {
        logger.info(String.format("checkPassword() login: %s, password: %s", login, password));
        String dbPassword = getUserPassword(login);
        if (dbPassword == null) {
            throw new Exception("Invalid login + password");
        }
        return Password.check(password, dbPassword);
    }

    @Override
    public void confirmUserForToken(String token) throws SQLException {
        logger.info("confirmUserForToken() token: " + token);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark_db\".t_user_profile set confirmed=true where confirmed_token=?");
            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.info("confirmUserForToken() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<MessageDto> getChatHistory(String user1, String user2) throws SQLException {
        logger.info(String.format("getChatHistory() user1: %s, user2: %s", user1, user2));
        List<MessageDto> messages = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select t1.text, t1.date, t2.login as login1, t3.login as login2 from \"spark_db\".t_message t1\n" +
                    " join \"spark_db\".t_user_profile t2 on (t1.\"from\"=t2.user_profile_id)\n" +
                    " join \"spark_db\".t_user_profile t3 on (t1.\"to\"=t3.user_profile_id)\n" +
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
        }
        return messages;
    }

    @Override
    public void saveChatMessage(MessageDto messageDto) throws Exception {
        logger.info("saveChatMessage()");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark_db\".t_message (text, date, \"from\", \"to\") VALUES\n" +
                    "(?\n" +
                    " ,?\n" +
                    " ,(select user_profile_id from \"spark_db\".t_user_profile where login=?)\n" +
                    " ,(select user_profile_id from \"spark_db\".t_user_profile where login=?))");
            preparedStatement.setString(1, messageDto.getMsgText());
            preparedStatement.setLong(2, messageDto.getDate());
            preparedStatement.setString(3, messageDto.getFrom());
            preparedStatement.setString(4, messageDto.getTo());
            preparedStatement.execute();
            logger.info(mapper.writeValueAsString(messageDto));
        } catch (SQLException ex) {
            logger.info("saveChatMessage() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void saveImage(String user, String image) throws SQLException {
        logger.info(String.format("saveImage(%s)", user));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into \"spark_db\".t_image (user_id, bytes) values" +
                    " ((select user_profile_id from \"spark_db\".t_user_profile where login=? limit 1), ?)");
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, image);
            preparedStatement.executeQuery();
        } catch (SQLException ex) {
            logger.info("saveImage() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void deleteImage(String user, String id) throws SQLException, AccessDeniedException {
        logger.info(String.format("deleteImage(%s, %s)", id, user));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select (select login from \"spark_db\".t_user_profile where user_profile_id=user_id limit 1) as user" +
                    " from \"spark_db\".t_image where id_image=?");
            preparedStatement.setString(1, user);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (user.equals(resultSet.getString("id_image")))
                throw new AccessDeniedException("this photo does not belong to the current user!");
            deleteImage(id);
        } catch (SQLException ex) {
            logger.info("deleteImage() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    private void deleteImage(String id) throws SQLException {
        logger.info(String.format("deleteImage(%s)", id));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from \"spark_db\".t_image where id_image=?");
            preparedStatement.setInt(1, Integer.parseInt(id));
            preparedStatement.execute();
        } catch (SQLException ex) {
            logger.info("deleteImage() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void setMainImage(String imageId, String userLogin) throws SQLException {
        logger.info(String.format("setMainImage(%s)", imageId));
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update \"spark_db\".t_image set is_main=false " +
                    " where user_id=(select user_profile_id from \"spark_db\".t_user_profile where login=? limit 1)");
            preparedStatement.setString(1, userLogin);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("update \"spark_db\".t_image set is_main=true where id_image=?");
            preparedStatement.setInt(1, Integer.parseInt(imageId));
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.info("setMainImage() exception:\n" + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<UserPhotoDto> getUserPhotos(String user) throws SQLException {
        logger.info(String.format("getUserPhotos(%s)", user));
        List<UserPhotoDto> photos = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from \"spark_db\".t_image " +
                    " where user_id=(select user_profile_id from \"spark_db\".t_user_profile where login=? limit 1)");
            preparedStatement.setString(1, user);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                photos.add(new UserPhotoDto(rs.getInt("id_image"), rs.getBoolean("is_main"), rs.getString("bytes")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            logger.info("setMainImage() exception:\n" + ex.getMessage());
            throw ex;
        }
        return photos;
    }

    @Override
    public List<UserProfileDto> getUsersWithFilter(UserFilterDto filter, String login)
            throws SQLException, JsonProcessingException {
        logger.info(String.format("getUserWithFilter(%s)", filter == null ? "null" : filter.toString()));
        List<UserProfileDto> profiles = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(generateUserSearchRequest(filter, login));
            addValuesToPreparedStatement(preparedStatement, filter, login);
            preparedStatement.execute();
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                UserProfileDto userProfile = UserProfileDto.getInstance(rs);
                profiles.add(userProfile);
            }
            logger.info("getUsersWithFilter() result:\n" + mapper.writeValueAsString(profiles));
        } catch (SQLException | JsonProcessingException ex) {
            logger.info("getUserWithFilter() exception:\n" + ex.getMessage());
            throw ex;
        }
        return profiles;
    }
}
