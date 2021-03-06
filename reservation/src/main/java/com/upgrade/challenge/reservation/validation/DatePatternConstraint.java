package com.upgrade.challenge.reservation.validation;

import com.upgrade.challenge.reservation.validation.validator.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.text.SimpleDateFormat;

/**
 * Created by fernando on 10/02/19.
 */
@Documented
@Constraint(validatedBy = DateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DatePatternConstraint {
    String DATE_PATTERN = "yyyy-MM-dd";
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DatePatternConstraint.DATE_PATTERN);
    String message() default "Invalid date.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
