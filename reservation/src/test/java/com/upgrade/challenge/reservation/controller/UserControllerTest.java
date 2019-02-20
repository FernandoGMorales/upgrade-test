package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.repository.UserRepository;
import com.upgrade.challenge.reservation.service.UserService;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.LongStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.*;

/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs
public class UserControllerTest extends BaseController {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "http://localhost:";
    private String endpoint = "/users/save";

    @After
    public void clearDB() {
        repository.deleteAll();
    }

    @Test
    public void givenUserWithValidData_whenSaveIsSuccessful_thenAJsonWithUserDataIsRetrieved() throws Exception {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser("Alexander", "The Great", "alexander.magnus@gmail.com", reservation);

        String url = new StringBuilder().append(baseUrl).append(port).append(endpoint).toString();
        ResponseEntity<String> result = doPost(user, restTemplate, url);

        String json = result.getBody();
        SimpleDateFormat sdf = DatePatternConstraint.DATE_FORMAT;
        String sd1 = sdf.format(start);
        String sd2 = sdf.format(end);

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.email", equalTo("alexander.magnus@gmail.com")));
        assertThat(json, hasJsonPath("$.firstName", equalTo("Alexander")));
        assertThat(json, hasJsonPath("$.lastName", equalTo("The Great")));
        assertThat(json, hasJsonPath("$.reservation.startDate", containsString(sd1)));
        assertThat(json, hasJsonPath("$.reservation.endDate", containsString(sd2)));
        assertThat(json, hasJsonPath("$.reservation.id", is(greaterThan(0))));
    }

    @Test
    public void givenUserWithValidData_whenSave_andThereIsReservationInThatPeriod_thenAJsonWithErrorDetailsIsRetrieved()
            throws Exception {
        //User 1
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser("Alexander", "The Great", "alexander.magnus@gmail.com", reservation);

        String url = new StringBuilder().append(baseUrl).append(port).append(endpoint).toString();
        ResponseEntity<String> result1 = doPost(user, restTemplate, url);

        //User 2
        User user2 = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        ResponseEntity<String> result2 = doPost(user2, restTemplate, url);

        String json1 = result1.getBody();
        String json2 = result2.getBody();

        //user1 has been saved
        assertEquals(HttpStatus.OK.value(), result1.getStatusCodeValue());
        assertThat(json1, isJson());
        assertThat(json1, hasJsonPath("$.email", equalTo("alexander.magnus@gmail.com")));
        assertThat(json1, hasJsonPath("$.firstName", equalTo("Alexander")));
        assertThat(json1, hasJsonPath("$.lastName", equalTo("The Great")));

        //user2 has been rejected
        assertEquals(HttpStatus.OK.value(), result2.getStatusCodeValue());
        assertThat(json2, isJson());
        assertThat(json2, hasJsonPath("$.message", equalTo("There has been an error while processing the request.")));
        assertThat(json2, hasJsonPath("$.cause", equalTo("There exists reservations in that period. Please check availability.")));
        assertThat(json2, hasJsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void givenUserWithoutName_whenSave_thenAJsonWithErrorDetailsIsRetrieved() throws Exception {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser(null, null, "alexander.magnus@gmail.com", reservation);

        StringBuilder url = new StringBuilder().append(baseUrl).append(port).append(endpoint);
        ResponseEntity<String> result = doPost(user, restTemplate, url.toString());
        String json = result.getBody();

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.message", equalTo("There has been an error while processing the request.")));
        assertThat(json, hasJsonPath("$.cause", containsString("Name cannot be null.")));
        assertThat(json, hasJsonPath("$.cause", containsString("Last name cannot be null")));
        assertThat(json, hasJsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void givenUserWithoutEndDate_whenSave_thenAJsonWithErrorDetailsIsRetrieved() throws Exception {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Reservation reservation = getReservation(start, null, null);
        User user = getUser("Alexander", "The Great", "alexander.magnus@gmail.com", reservation);

        StringBuilder url = new StringBuilder().append(baseUrl).append(port).append(endpoint);
        ResponseEntity<String> result = doPost(user, restTemplate, url.toString());
        String json = result.getBody();

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.message", equalTo("There has been an error while processing the request.")));
        assertThat(json, hasJsonPath("$.cause", containsString("Invalid date.")));
        assertThat(json, hasJsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void givenUserWithoutEmail_whenSave_thenAJsonWithErrorDetailsIsRetrieved() throws Exception {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser("Alexander", "The Great", "", reservation);

        StringBuilder url = new StringBuilder().append(baseUrl).append(port).append(endpoint);
        ResponseEntity<String> result = doPost(user, restTemplate, url.toString());
        String json = result.getBody();

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.message", equalTo("There has been an error while processing the request.")));
        assertThat(json, hasJsonPath("$.cause", containsString("Email cannot be null.")));
        assertThat(json, hasJsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void givenUserWithoutReservation_whenSave_thenAJsonWithErrorDetailsIsRetrieved() throws Exception {
        User user = getUser("Alexander", "The Great", "alexander.magnus@gmail.com", null);

        StringBuilder url = new StringBuilder().append(baseUrl).append(port).append(endpoint);
        ResponseEntity<String> result = doPost(user, restTemplate, url.toString());
        String json = result.getBody();

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.message", equalTo("There has been an error while processing the request.")));
        assertThat(json, hasJsonPath("$.cause", equalTo("Reservation is missing.")));
        assertThat(json, hasJsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void givenEmptyUser_whenSave_thenAJsonWithErrorDetailsIsRetrieved() throws Exception {
        StringBuilder url = new StringBuilder().append(baseUrl).append(port).append(endpoint);
        ResponseEntity<String> result = doPost(new User(), restTemplate, url.toString());
        String json = result.getBody();

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.message", equalTo("There has been an error while processing the request.")));
        assertThat(json, hasJsonPath("$.cause", equalTo("Reservation is missing.")));
        assertThat(json, hasJsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void givenNullUser_whenSave_thenAJsonWithErrorDetailsIsRetrieved() throws Exception {
        StringBuilder url = new StringBuilder().append(baseUrl).append(port).append(endpoint);
        ResponseEntity<String> result = doPost(null, restTemplate, url.toString());
        String json = result.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.message", equalTo("Not a valid endpoint!")));
        assertThat(json, hasJsonPath("$.cause", equalTo("/users/save")));
        assertThat(json, hasJsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(3000);
        return clientHttpRequestFactory;
    }


    private String getJson(String d1, String d2) {
        return "{\"email\":\"alexander.magnus@gmail.com\",\"firstName\":\"Alexander\",\"lastName\":\"The Great\",\"reservation\":{\"startDate\":\"" +
                d1 + "\",\"" +
                d2 +"\":\"2019-02-21\"}}";
    }

}
