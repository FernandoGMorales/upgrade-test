package com.upgrade.challenge.reservation.validation;

import com.upgrade.challenge.reservation.validation.validator.ReservationValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fernando on 10/02/19.
 */
@Constraint(validatedBy = ReservationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservationConstraint {
    String message() default "Date range is invalid.";

    String startDate();

    String endDate();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ReservationConstraint[] value();
    }
}
