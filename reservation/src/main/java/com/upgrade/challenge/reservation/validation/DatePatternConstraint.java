package com.upgrade.challenge.reservation.validation;

import com.upgrade.challenge.reservation.validation.validator.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by fernando on 10/02/19.
 */
@Documented
@Constraint(validatedBy = DateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DatePatternConstraint {
    String DATE_PATTERN = "yyyy-MM-dd";
    String message() default "Invalid date.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
