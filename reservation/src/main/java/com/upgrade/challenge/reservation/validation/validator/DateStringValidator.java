package com.upgrade.challenge.reservation.validation.validator;

import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import com.upgrade.challenge.reservation.validation.DateStringPatternConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by fernando on 13/02/19.
 */
public class DateStringValidator implements ConstraintValidator<DateStringPatternConstraint, String> {

    private final static Logger LOG = LoggerFactory.getLogger(DateValidator.class);
    private final static String WARN_MESSAGE = "Cannot parse the given string date: ";

    @Override
    public void initialize(DateStringPatternConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = false;
        try {
            DatePatternConstraint.DATE_FORMAT.parse(date);
            valid = true;
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE + date, e);
        }
        return valid;
    }
}
