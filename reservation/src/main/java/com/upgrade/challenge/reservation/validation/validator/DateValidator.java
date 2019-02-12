package com.upgrade.challenge.reservation.validation.validator;

import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fernando on 10/02/19.
 */
public class DateValidator implements ConstraintValidator<DatePatternConstraint, Date> {

    private final static Logger LOG = LoggerFactory.getLogger(DateValidator.class);
    private final static String WARN_MESSAGE = "Cannot parse the given date: ";
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DatePatternConstraint.DATE_PATTERN);

    @Override
    public void initialize(DatePatternConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = false;
        try {
            DATE_FORMAT.format(date);
            valid = true;
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE + date, e);
        }
        return valid;
    }
}
