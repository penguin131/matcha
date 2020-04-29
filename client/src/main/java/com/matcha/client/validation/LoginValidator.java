package com.matcha.client.validation;

import com.matcha.client.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class LoginValidator implements ConstraintValidator<LoginExists, String> {

    private UserService service;

    @Autowired
    public LoginValidator(UserService service) {
        this.service = service;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return service.getUserProfileForLogin(s) == null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
