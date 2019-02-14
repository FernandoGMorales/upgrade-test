package com.upgrade.challenge.reservation.validation;

import com.upgrade.challenge.reservation.validation.validator.DateStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by fernando on 13/02/19.
 */
@Documented
@Constraint(validatedBy = DateStringValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateStringPatternConstraint {
    String message() default "Invalid date.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
