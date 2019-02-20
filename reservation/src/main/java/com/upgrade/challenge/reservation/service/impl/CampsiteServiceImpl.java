package com.upgrade.challenge.reservation.service.impl;

import com.upgrade.challenge.reservation.controller.dto.Range;
import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.exception.CampsiteException;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.service.CampsiteService;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import com.upgrade.challenge.reservation.validation.validator.DateValidator;
import com.upgrade.challenge.reservation.validation.validator.ReservationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CampsiteServiceImpl implements CampsiteService {

    private final static Logger LOG = LoggerFactory.getLogger(ReservationValidator.class);
    private final static String WARN_MESSAGE = "Cannot verify range availability.";
    private final static int DEFAULT_RANGE = 30;

    @Autowired
    private ReservationRepository repository;

    @Future private Date startRange;
    @Future private Date endRange;

    @Override
    public List<Range> findByRange(Date date1, Date date2) throws CampsiteException {
        startRange = date1;
        endRange = date2;
        List<Range> range = null;
        try {
            List<Reservation> list = repository.findByRange(startRange, endRange);
            if(list.isEmpty()) {
                range = new ArrayList<>();
                range.add(
                        new Range(
                                DatePatternConstraint.DATE_FORMAT.format(startRange),
                                DatePatternConstraint.DATE_FORMAT.format(endRange))
                );
            }
            else {
                range = getRanges(list, startRange, endRange);
            }
        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new CampsiteException(WARN_MESSAGE);
        }
        return range;
    }

    private List<Range> getRanges(List<Reservation> reservations, Date date1, Date date2) {
        List<Range> ranges = new ArrayList<>();

        //Check range, otherwise use default range of one month.
        if(!isValidRange(date1, date2)) {
            date1 = new Date();
            date2 = getDate(LocalDate.now().plusMonths(1));
        }

        //Step 1: check if startDate of first reserv. is after start date of the range
        LocalDate first = LocalDate.parse(DatePatternConstraint.DATE_FORMAT.format(reservations.get(0).getStartDate()));
        LocalDate startRange = getLocalDate(date1);
        if(first.isAfter(startRange)) {
            ranges.add(new Range(startRange.toString(), first.toString()));
        }

        //Step 2: find ranges between enDate of prev reservation and startDate of current reserv

        LocalDate currentStartDate = null;
        LocalDate prevEnDate = null;
        for(int i=1; i<reservations.size(); i++) {
            prevEnDate = LocalDate.parse(DatePatternConstraint.DATE_FORMAT.format(reservations.get(i-1).getEndDate()));
            currentStartDate = LocalDate.parse(DatePatternConstraint.DATE_FORMAT.format(reservations.get(i).getStartDate()));
            int days = Period.between(prevEnDate, currentStartDate).getDays();
            if(days>0) {
                ranges.add(new Range(prevEnDate.toString(), currentStartDate.toString()));
            }
        }

        //Step 3: check if endDate of last reserv. is before the end date of the range
        LocalDate end = LocalDate.parse(DatePatternConstraint.DATE_FORMAT.format(reservations.get(reservations.size()-1).getEndDate()));
        LocalDate endRange = getLocalDate(date2);
        if(end.isBefore(endRange)) {
            ranges.add(new Range(end.toString(), endRange.toString()));
        }

        return ranges;
    }

    private LocalDate getLocalDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return LocalDate.parse(DatePatternConstraint.DATE_FORMAT.format(cal.getTime()));
    }

    /*
        Valid ranges:
        1- start date before end date
        2- start date after current date
     */
    private boolean isValidRange(Date date1, Date date2) {
        boolean valid = false;
        int days = -1;
        valid = date1.toInstant().isAfter(new Date().toInstant());
        valid = valid && date2.toInstant().isAfter(date1.toInstant());
        return valid;
    }

    private Date getDate(LocalDate d1) {
        return Date.from(d1.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
