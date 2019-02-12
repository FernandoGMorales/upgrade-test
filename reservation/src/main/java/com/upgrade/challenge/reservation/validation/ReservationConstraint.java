package com.upgrade.challenge.reservation.validation;

import com.upgrade.challenge.reservation.validation.validator.ReservationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by fernando on 10/02/19.
 */
@Documented
@Constraint(validatedBy = ReservationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservationConstraint {
    String message() default "Date range is invalid.";
    String startDate();
    String endDate();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ReservationConstraint[] value();
    }
}
