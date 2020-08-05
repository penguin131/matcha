package com.service;

import com.dto.BaseUserProfileDto;
import com.dto.FriendDto;
import com.dto.MessageDto;
import com.dto.UserProfileDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    List<UserProfileDto> getAllUsers() throws SQLException, JsonProcessingException;
    void createUserProfile(BaseUserProfileDto userProfileDto, String confirmed_token) throws Exception;
    UserProfileDto getUserProfileForLogin(String login) throws SQLException, JsonProcessingException;
    List<FriendDto> getAllFriendsForLogin(String login) throws SQLException;
    void setLike(String from, String to) throws Exception;
    void deleteUserProfileForLogin(String login) throws SQLException;
    void updateUserProfile(UserProfileDto userProfileDto) throws SQLException, JsonProcessingException;
    boolean checkPassword(String login, String password) throws Exception;
    void confirmUserForToken(String token) throws SQLException;
    List<MessageDto> getChatHistory(String user1, String user2) throws SQLException;
    void saveChatMessage(MessageDto messageDto) throws Exception;
    String saveImage(String user) throws SQLException;
    void deleteImage(String id) throws SQLException;
}
