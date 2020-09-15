package com.helper;

import com.dictionary.Sex;
import com.dto.BaseUserProfileDto;
import com.dto.UserProfileDto;
import com.exceptions.ValidateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.DatabaseService;
import spark.utils.StringUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.SQLException;

public class ValidateHelper {
    private static DatabaseService service = DatabaseServiceHelper.getDatabaseService();

    public static void validateBaseUserProfile(BaseUserProfileDto baseUserProfile) throws ValidateException, AddressException, SQLException, JsonProcessingException {
        if (baseUserProfile == null)
            throw new ValidateException("Null user profile");
        if (baseUserProfile.getLogin() == null ||
                baseUserProfile.getLogin().length() < 5 && baseUserProfile.getLogin().length() > 256)
            throw new ValidateException("Login size must be between 5 and 256");
        if (StringUtils.isEmpty(baseUserProfile.getPassword()))
            throw new ValidateException("Password cannot be empty");
//        if (Sex.containsCode(baseUserProfile.getSex()))
//            throw new ValidateException("Non-existent gender");
        validateEmail(baseUserProfile.getEmail());
        if (service.getUserProfileForLogin(baseUserProfile.getLogin()) != null) {
            throw new ValidateException("Login already exists!");
        }
        if (service.checkEmailExist(baseUserProfile.getEmail())) {
            throw new ValidateException("Email is already in use");
        }
    }

    private static void validateEmail(String email) throws ValidateException, AddressException {
        if (StringUtils.isEmpty(email))
            throw new ValidateException("Empty email");
        InternetAddress emailAddr = new InternetAddress(email);
        emailAddr.validate();
    }

    public static void validateUserProfile(UserProfileDto userProfileDto) throws ValidateException, AddressException, SQLException, JsonProcessingException {
        validateBaseUserProfile(userProfileDto);
//        if (userProfileDto.getSexPreferences() != null && Sex.containsCode(userProfileDto.getSexPreferences()))
//            throw new ValidateException("Non-existent gender in sex preferences");
    }
}
