package com.upgrade.challenge.reservation.validation.validator;

import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import com.upgrade.challenge.reservation.validation.ReservationConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

/**
 * Created by fernando on 11/02/19.
 */
public class ReservationValidator implements ConstraintValidator<ReservationConstraint, Object> {

    private final static Logger LOG = LoggerFactory.getLogger(ReservationValidator.class);
    private final static int MAX_DAYS_TO_CONFIRM_RESERVATION_IN_ADVANCE = 30;
    private final static int MIN_DAYS_TO_CONFIRM_RESERVATION_IN_ADVANCE = 1;
    private final static int MIN_DAYS_TO_RESERVE = 1;
    private final static int MAX_DAYS_TO_RESERVE = 3;
    private final static String WARN_MESSAGE = "Invalid date range!";

    private String startDate;
    private String endDate;

    @Override
    public void initialize(ReservationConstraint constraintAnnotation) {
        this.startDate = constraintAnnotation.startDate();
        this.endDate = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        return isAllowedToReserve(
                (Date) new BeanWrapperImpl(value).getPropertyValue(startDate),
                (Date) new BeanWrapperImpl(value).getPropertyValue(endDate)
        );
    }

    private boolean isAllowedToReserve(Date date1, Date date2) {
        int days = -1;
        try {
            days = Period.between(LocalDate.now(), LocalDate.parse(DatePatternConstraint.DATE_FORMAT.format(date1))).getDays();
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE, e);
        }
        return (days>=MIN_DAYS_TO_CONFIRM_RESERVATION_IN_ADVANCE &&
                days<=MAX_DAYS_TO_CONFIRM_RESERVATION_IN_ADVANCE) &&
                isValidRange(date1, date2);
    }

    private boolean isValidRange(Date date1, Date date2) {
        int days = -1;
        try {
            days = Period.between(
                    LocalDate.parse(DatePatternConstraint.DATE_FORMAT.format(date1)),
                    LocalDate.parse(DatePatternConstraint.DATE_FORMAT.format(date2))).getDays();
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE, e);
        }
        return (days>=MIN_DAYS_TO_RESERVE && days<=MAX_DAYS_TO_RESERVE);
    }
}
