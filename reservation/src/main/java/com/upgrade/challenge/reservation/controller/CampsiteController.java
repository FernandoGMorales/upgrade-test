package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.service.CampsiteService;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class CampsiteController {

    @Autowired
    private CampsiteService service;

    @GetMapping("/reservation/startDate")
    public List<?> findByRange(
            @RequestParam(value="date1") @DatePatternConstraint String date1,
            @RequestParam(value="date2") @DatePatternConstraint String date2) throws ReservationException {
        return service.findByRange(date1, date2);
    }
}
