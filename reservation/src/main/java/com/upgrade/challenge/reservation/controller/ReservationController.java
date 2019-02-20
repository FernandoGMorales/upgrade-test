package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.exception.EntityNotFoundException;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.exception.format.ResponseMsg;
import com.upgrade.challenge.reservation.service.ReservationService;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fernando on 10/02/19.
 */
@RestController
public class ReservationController {

    @Autowired
    private ReservationService service;

    @DeleteMapping("/reservations/cancel/{id}")
    public Map<String, Reservation> cancel(@PathVariable Long id) throws ReservationException, EntityNotFoundException {
        return Collections.singletonMap("deleted", service.cancel(id));
    }

    @PostMapping("/reservations/modify")
    public Reservation modify(@RequestBody Reservation reservation) throws ReservationException, EntityNotFoundException {
        return service.modify(reservation);
    }

}
