package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.service.ReservationService;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Date;

/**
 * Created by fernando on 10/02/19.
 */
@RestController
@Validated
public class ReservationController {

    @Autowired
    private ReservationService service;

    @GetMapping("/reservations/startDate")
    public Reservation findByStartDate(@RequestParam(value="date") @DatePatternConstraint Date date) throws ReservationException {
        return service.findByStartDate(date);
    }

    @GetMapping("/reservations/endDate")
    public Reservation findByEndDate(@RequestParam(value="date") @DatePatternConstraint Date date) throws ReservationException {
        return service.findByEndDate(date);
    }

    @PostMapping("/reservations/save")
    public Reservation save(@RequestBody @Valid Reservation reservation) throws ReservationException {
        return service.save(reservation);
    }

    @GetMapping("/reservations/cancel")
    public void cancel(@RequestParam(value="id") @Positive Long id) throws ReservationException {
        service.cancel(id);
    }

    @PostMapping("/reservations/modify")
    public Reservation modify(@RequestBody @Valid Reservation reservation) throws ReservationException {
        return service.modify(reservation);
    }

}
