package com.helper;

import com.dto.BaseUserProfileDto;
import com.dto.InnerProfileDto;
import com.exceptions.ValidateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.DatabaseService;
import org.apache.log4j.Logger;
import spark.utils.StringUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.SQLException;

public class ValidateHelper {
    private static Logger logger = Logger.getLogger(ValidateHelper.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private static DatabaseService service = ServiceHelper.getDatabaseService();

    public static void validateBaseUserProfile(BaseUserProfileDto baseUserProfile)
            throws ValidateException, AddressException, SQLException, JsonProcessingException {
        logger.info(String.format("==>  validateBaseUserProfile(%s)", mapper.writeValueAsString(baseUserProfile)));
        if (baseUserProfile == null)
            throw new ValidateException("Null user profile");
        if (baseUserProfile.getLogin() == null ||
                baseUserProfile.getLogin().length() < 5 && baseUserProfile.getLogin().length() > 256)
            throw new ValidateException("Login size must be between 5 and 256");
        if (StringUtils.isEmpty(baseUserProfile.getPassword()))
            throw new ValidateException("Password cannot be empty");
        validateEmail(baseUserProfile.getEmail());
        if (service.getUserProfileForLogin(baseUserProfile.getLogin()) != null) {
            throw new ValidateException("Login already exists!");
        }
        if (service.checkEmailExist(baseUserProfile.getEmail())) {
            throw new ValidateException("Email is already in use");
        }
        logger.info("<==    validateBaseUserProfile(): success");
    }

    private static void validateEmail(String email) throws ValidateException, AddressException {
        if (StringUtils.isEmpty(email))
            throw new ValidateException("Empty email");
        InternetAddress emailAddr = new InternetAddress(email);
        emailAddr.validate();
    }

    public static void validateUserProfile(InnerProfileDto userProfileDto)
            throws ValidateException, SQLException, JsonProcessingException {
        logger.info(String.format("==>  validateUserProfile(%s)", mapper.writeValueAsString(userProfileDto)));
        if (userProfileDto == null)
            throw new ValidateException("Null user profile");
        if (userProfileDto.getLogin() != null &&
                userProfileDto.getLogin().length() < 5 && userProfileDto.getLogin().length() > 256)
            throw new ValidateException("Login size must be between 5 and 256");
        if (userProfileDto.getLogin() != null && service.getUserProfileForLogin(userProfileDto.getLogin()) != null) {
            throw new ValidateException("Login already exists!");
        }
        if (userProfileDto.getEmail() != null && service.checkEmailExist(userProfileDto.getEmail())) {
            throw new ValidateException("Email is already in use");
        }
        logger.info("<==    validateUserProfile(): success");
    }
}
