package com.service;

import com.dto.*;
import com.exceptions.AccessDeniedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.dto.UserPhotoDto;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    List<UserProfileDto> getAllUsers(String login) throws SQLException, JsonProcessingException;
    void createUserProfile(BaseUserProfileDto userProfileDto, String confirmed_token) throws Exception;
    UserProfileDto getUserProfileForLogin(String login) throws SQLException, JsonProcessingException;
    List<FriendDto> getAllFriendsForLogin(String login) throws SQLException;
    void setLike(String from, String to) throws Exception;
    void setComplaint(String from, String to) throws SQLException;
    void deleteUserProfileForLogin(String login) throws SQLException;
    void updateUserProfile(UserProfileDto userProfileDto) throws SQLException, JsonProcessingException;
    boolean checkPassword(String login, String password) throws Exception;
    void confirmUserForToken(String token) throws SQLException;
    List<MessageDto> getChatHistory(String user1, String user2) throws SQLException;
    void saveChatMessage(MessageDto messageDto) throws Exception;
    String saveImage(String user) throws SQLException;
    void deleteImage(String user, String id) throws SQLException, AccessDeniedException;
    void setMainImage(String imageId, String userLogin) throws SQLException;
    List<UserPhotoDto> getUserPhotos(String user) throws SQLException;
    List<UserProfileDto> getUsersWithFilter(UserFilterDto filter, String login) throws SQLException, JsonProcessingException;
}
