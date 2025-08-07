package com.example.booking_hotel.validator;

import java.lang.annotation.*;
import java.lang.annotation.ElementType.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface DobConstraint {

    String message() default "Invalid Date";

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
