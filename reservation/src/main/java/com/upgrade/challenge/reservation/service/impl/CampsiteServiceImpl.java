package com.upgrade.challenge.reservation.service.impl;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.service.CampsiteService;
import com.upgrade.challenge.reservation.validation.validator.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CampsiteServiceImpl implements CampsiteService {

    @Autowired
    private ReservationRepository repository;

    @Override
    public List<Range> findByRange(String date1, String date2) {
        return searchRanges(date1, date2, repository.findByRange(date1, date2));
    }

    private List<Range> searchRanges(String date1, String date2, Stream<Reservation> rStream) {
        List<Range> rangeList = null;

//        rStream.map(
//                x -> new Range(
//                        DateValidator.DATE_FORMAT.format(x.getStartDate()),
//                        DateValidator.DATE_FORMAT.format(x.getEndDate())
//                );

        return rangeList;
    }

    private List<Range> getRanges(List<Reservation> reservations, Date date1, Date date2) {
        List<Range> ranges = new ArrayList<>();

        //Step 1: check if startDate of first reserv. is after start date of the range
        LocalDate first = getLocalDate(reservations.get(0).getStartDate());
        LocalDate startRange = getLocalDate(date1);
        if(first.isAfter(startRange)) {
            ranges.add(new Range(startRange.toString(), first.toString()));
        }

        //Step 2: find ranges between enDate of prev reservation and startDate of current reserv

        LocalDate currentStartDate = null;
        LocalDate prevEnDate = null;
        for(int i=1; i<reservations.size(); i++) {
            prevEnDate = getLocalDate(reservations.get(i-1).getEndDate());
            currentStartDate = getLocalDate(reservations.get(i).getStartDate());
            int days = Period.between(prevEnDate, currentStartDate).getDays();
            if(days>0) {
                ranges.add(new Range(prevEnDate.toString(), currentStartDate.toString()));
            }
        }

        return ranges;
    }

    private LocalDate getLocalDate(Date date) {
        return LocalDate.parse(DateValidator.DATE_FORMAT.format(date));
    }


    public class Range {
        private String date1;
        private String date2;

        public Range(String date1, String date2) {
            this.date1 = date1;
            this.date2 = date2;
        }

        public String getDate1() {
            return date1;
        }

        public void setDate1(String date1) {
            this.date1 = date1;
        }

        public String getDate2() {
            return date2;
        }

        public void setDate2(String date2) {
            this.date2 = date2;
        }
    }


}
