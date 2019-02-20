package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.repository.UserRepository;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by fernando on 18/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseController {

    private final static String BASE_URL = "http://localhost:";
    static final String SAVE_ENDPOINT = "/users/save";
    static final String CANCEL_ENDPOINT = "/reservations/cancel/";
    static final String MODIFY_ENDPOINT = "/reservations/modify";
    static final String CAMPSITE_START_ENDPOINT = "/campsite/availability/start/";
    static final String CAMPSITE_END_ENDPOINT = "/end/";

    @LocalServerPort
    private int port;

    @Autowired
    UserRepository repository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TestRestTemplate restTemplate;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @After
    public void clearDB() {
        repository.deleteAll();
    }

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

    Reservation getReservation(Date startDate, Date endDate, Long id) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setId(id);
        return reservation;
    }

    <T> ResponseEntity<String> doPost(T entity, String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<T> request = new HttpEntity<>(entity, headers);
        String url = new StringBuilder().append(BASE_URL).append(port).append(endpoint).toString();
        return restTemplate.postForEntity(URI.create(url), request, String.class);
    }

    protected ResponseEntity<String> doGet(String endpoint, Object...variables) {
        return restTemplate.getForEntity(BASE_URL + port + endpoint, String.class, variables);
    }

    void doDelete(String endpoint) {
        restTemplate.delete(BASE_URL + port + endpoint);
    }

}
