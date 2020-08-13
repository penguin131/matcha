package com.helper;

import com.dictionary.Sex;
import com.dto.BaseUserProfileDto;
import com.dto.UserProfileDto;
import com.exceptions.ValidateException;
import spark.utils.StringUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ValidateHelper {
    public static void validateBaseUserProfile(BaseUserProfileDto baseUserProfile) throws ValidateException, AddressException {
        if (baseUserProfile.getLogin() == null ||
                baseUserProfile.getLogin().length() < 5 && baseUserProfile.getLogin().length() > 256)
            throw new ValidateException("Login size must be between 5 and 256");
        if (StringUtils.isEmpty(baseUserProfile.getPassword()))
            throw new ValidateException("Password cannot be empty");
        if (Sex.containsCode(baseUserProfile.getSex()))
            throw new ValidateException("Non-existent gender");
        validateEmail(baseUserProfile.getEmail());
    }

    private static void validateEmail(String email) throws ValidateException, AddressException {
        if (StringUtils.isEmpty(email))
            throw new ValidateException("Empty email");
        InternetAddress emailAddr = new InternetAddress(email);
        emailAddr.validate();
    }

    public static void validateUserProfile(UserProfileDto userProfileDto) throws ValidateException, AddressException {
        validateBaseUserProfile(userProfileDto);
        if (userProfileDto.getSexPreferences() != null && Sex.containsCode(userProfileDto.getSexPreferences()))
            throw new ValidateException("Non-existent gender in sex preferences");
    }
}
