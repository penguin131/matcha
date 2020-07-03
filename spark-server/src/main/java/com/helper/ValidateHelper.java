package com.helper;

import com.dictionary.Sex;
import com.dto.BaseUserProfileDto;
import spark.utils.StringUtils;

import javax.mail.internet.InternetAddress;

public class ValidateHelper {
    public static void validateBaseUserProfile(BaseUserProfileDto baseUserProfile) throws Exception {
        if (baseUserProfile.getLogin() == null ||
                baseUserProfile.getLogin().length() < 5 && baseUserProfile.getLogin().length() > 256)
            throw new Exception("Login size must be between 5 and 256");
        if (StringUtils.isEmpty(baseUserProfile.getPassword()))
            throw new Exception("Password cannot be empty");
        if (!Sex.containsCode(baseUserProfile.getSex()))
            throw new Exception("Non-existent gender");
        validateEmail(baseUserProfile.getEmail());
    }

    private static void validateEmail(String email) throws Exception {
        if (StringUtils.isEmpty(email))
            throw new Exception("Empty email");
        InternetAddress emailAddr = new InternetAddress(email);
        emailAddr.validate();
    }
}
