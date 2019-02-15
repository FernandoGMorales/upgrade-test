package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.controller.dto.Range;
import com.upgrade.challenge.reservation.exception.CampsiteException;
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
    private final static String PARSE_DATE_ERROR = "There has been an error while parsing the input date.";

    @Autowired
    private CampsiteService service;

    @GetMapping("/campsite/availability")
    public List<Range> findByRange(
            @RequestParam(value="date1") @DateStringPatternConstraint String date1,
            @RequestParam(value="date2") @DateStringPatternConstraint String date2) throws CampsiteException {
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = DatePatternConstraint.DATE_FORMAT.parse(date1);
            d2 = DatePatternConstraint.DATE_FORMAT.parse(date2);
        } catch(Exception e) {
            LOG.error(PARSE_DATE_ERROR, e);
            throw new CampsiteException(PARSE_DATE_ERROR);
        }
        return service.findByRange(d1, d2);
    }
}
