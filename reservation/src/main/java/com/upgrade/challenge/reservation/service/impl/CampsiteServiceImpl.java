package com.upgrade.challenge.reservation.service.impl;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.service.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CampsiteServiceImpl implements CampsiteService {

    @Autowired
    private ReservationRepository repository;

    @Override
    public List<Tuple<String, String>> rangesAvailable(String startDate, String endDate) {
        return searchRanges(startDate, endDate, repository.findByStartDateOrEndDateOrderByStartDate(startDate, endDate));
    }

    private List<Tuple<String,String>> searchRanges(String startDate, String endDate, Stream<Reservation> rStream) {
        List<Tuple<String,String>> rangeList = null;

        rStream.map(x -> new Tuple(x.getStartDate(), x.getEndDate()));

        return rangeList;
    }

    public class Tuple<U, E> {
        U value1;
        E value2;

        public Tuple(U value1, E value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        public U getValue1() {
            return value1;
        }

        public void setValue1(U value1) {
            this.value1 = value1;
        }

        public E getValue2() {
            return value2;
        }

        public void setValue2(E value2) {
            this.value2 = value2;
        }
    }


}
