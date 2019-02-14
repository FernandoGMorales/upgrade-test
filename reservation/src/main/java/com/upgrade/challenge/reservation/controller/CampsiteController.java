package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.controller.dto.Range;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.service.CampsiteService;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import com.upgrade.challenge.reservation.validation.DateStringPatternConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@Validated
public class CampsiteController {

    private final static Logger LOG = LoggerFactory.getLogger(CampsiteController.class);
    private final static String WARN_MESSAGE = "There has been an error while processing this request.";

    @Autowired
    private CampsiteService service;

    @GetMapping("/campsite/availability")
    public List<Range> findByRange(
            @RequestParam(value="date1") @DateStringPatternConstraint String date1,
            @RequestParam(value="date2") @DateStringPatternConstraint String date2) throws ReservationException {
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = DatePatternConstraint.DATE_FORMAT.parse(date1);
            d2 = DatePatternConstraint.DATE_FORMAT.parse(date2);
        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new ReservationException(WARN_MESSAGE);
        }
        return service.findByRange(d1, d2);
    }
}
