package com.matcha.client.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= LoginValidator.class)
public @interface LoginExists {
    String message() default "Login already exists!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
