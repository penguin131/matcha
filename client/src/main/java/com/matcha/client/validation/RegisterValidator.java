//package com.matcha.client.validation;
//
//import com.matcha.client.form.RegisterForm;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//public class RegisterValidator implements Validator {
//    // javax.validation.Validator
//    @Autowired
//    private Validator validator;
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return RegisterForm.class.equals(aClass);
//    }
//
//    @Override
//    public void validate(Object obj, Errors errors) {
//        validator.validate(obj, errors);
//
//
//    }
//}
