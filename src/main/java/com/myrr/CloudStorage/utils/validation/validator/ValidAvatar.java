package com.myrr.CloudStorage.utils.validation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidAvatarValidator.class)
public @interface ValidAvatar {
    String message() default "Invalid avatar";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
