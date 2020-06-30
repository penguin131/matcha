package com.matcha.helper;

import com.matcha.dto.UserProfileDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserProfileValidator {
    private Connection connection;

    public void validateUserProfile(UserProfileDto userProfile) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from my_db.t_user_profile where login=? or email=?");
        preparedStatement.setString(1, userProfile.getLogin());
        preparedStatement.setString(2, userProfile.getEmail());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            String errorString;
            if (rs.getString("login").equals(userProfile.getLogin()))
                errorString = String.format("Login %s already exists!", userProfile.getLogin());
            else
                errorString = String.format("Email %s is already in use", userProfile.getEmail());
            throw new Exception(errorString);
        }
    }

    public UserProfileValidator(Connection connection) {
        this.connection = connection;
    }
}
