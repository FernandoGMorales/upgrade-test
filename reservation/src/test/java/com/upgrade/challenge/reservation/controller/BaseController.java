package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by fernando on 18/02/19.
 */
public class BaseController {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    protected String getDateAsString(Date date) {
        return DatePatternConstraint.DATE_FORMAT.format(date);
    }

    protected Date getDate(LocalDate d1) {
        return Date.from(d1.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    protected User getUser(String name, String lastname, String email, Reservation reservation) {
        User user = new User();
        user.setFirstName(name);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setReservation(reservation);
        return user;
    }

    protected Reservation getReservation(Date startDate, Date endDate, Long id) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setId(id);
        return reservation;
    }

    protected ResponseEntity<String> doPost(User user, TestRestTemplate restTemplate, String baseUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        return restTemplate.postForEntity(URI.create(baseUrl), request, String.class);
    }

}
