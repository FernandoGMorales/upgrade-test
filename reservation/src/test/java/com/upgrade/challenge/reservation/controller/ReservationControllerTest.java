package com.upgrade.challenge.reservation.controller;

import com.jayway.jsonpath.JsonPath;
import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.EntityNotFoundException;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.*;


/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationControllerTest extends BaseController {

    @Autowired
    private ReservationController controller;

    @Test
    public void givenValidId_whenCancel_thenReservationIsDeletedFromDB() {
        // 1 - Saves user
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser("Marcvs Tvlivs", "Cicero", "marcvs.cicero@gmail.com", reservation);

        ResponseEntity<String> result = doPost(user, SAVE_ENDPOINT);
        String json = result.getBody();
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());

        // 2 - Cancel user reservation (deletes both, user and reservation)
        int id = JsonPath.read(json, "$.reservation.id");
        doDelete(CANCEL_ENDPOINT + (long)id);

        // 3 - verify reservation has been deleted from db
        assertThat(repository.findByReservationId((long)id), equalTo(null));
    }

    @Test
    public void givenInvalidId_whenCancel_thenJsonWithErrorDetailsIsRetrieved() throws ReservationException, EntityNotFoundException {
        Long id = (long)(Math.random()*10000);
        exceptionRule.expect(ReservationException.class);
        exceptionRule.expect(hasProperty("msg", equalTo("There isn't any reservation with id=" + id + ".")));
        controller.cancel(id);
    }

    @Test
    public void givenNullId_whenCancel_thenJsonWithErrorDetailsIsRetrieved() throws ReservationException, EntityNotFoundException {
        exceptionRule.expect(ReservationException.class);
        exceptionRule.expect(hasProperty("msg", equalTo("Missing reservation id.")));
        controller.cancel(null);
    }

    @Test
    public void givenValidId_whenModify_andThereIsAvailability_thenReservationIsUpdated() {
        SimpleDateFormat sdf = DatePatternConstraint.DATE_FORMAT;

        // 1 - Saves user and reservation.
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser("Marcvs Tvlivs", "Cicero", "marcvs.cicero@gmail.com", reservation);

        String sd1 = sdf.format(start);
        String sd2 = sdf.format(end);

        ResponseEntity<String> result = doPost(user, SAVE_ENDPOINT);
        String json = result.getBody();
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.reservation.startDate", containsString(sd1)));
        assertThat(json, hasJsonPath("$.reservation.endDate", containsString(sd2)));
        assertThat(json, hasJsonPath("$.reservation.id", is(greaterThan(0))));

        // 2 - Modify user reservation.
        start = getDate(d1.plusDays(10));
        end = getDate(d1.plusDays(12));
        int id = JsonPath.read(json, "$.reservation.id");
        reservation.setId((long)id);
        reservation.setStartDate(start);
        reservation.setEndDate(end);
        result = doPost(reservation, MODIFY_ENDPOINT);
        json = result.getBody();

        // 3 - Assert that reservation has been modified.
        sd1 = sdf.format(start);
        sd2 = sdf.format(end);

        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.startDate", containsString(sd1)));
        assertThat(json, hasJsonPath("$.endDate", containsString(sd2)));
        assertThat(json, hasJsonPath("$.id", is(greaterThan(0))));
    }

    @Test
    public void givenValidId_whenModify_andThereIsNotAvailability_thenJsonWithErrorDetailsIsRetrieved() {
        SimpleDateFormat sdf = DatePatternConstraint.DATE_FORMAT;

        // 1 - Saves user 1.
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation r1 = getReservation(start, end, null);
        User cicero = getUser("Marcvs Tvlivs", "Cicero", "marcvs.cicero@gmail.com", r1);
        cicero = repository.saveAndFlush(cicero);

        // 2 - Saves user 2.
        start = getDate(d1.plusDays(4));
        end = getDate(d1.plusDays(5));
        Reservation r2 = getReservation(start, end, null);
        User caesar = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", r2);
        caesar = repository.saveAndFlush(caesar);

        // 2 - Modify user 1 reservation with same reservation of user 2.
        // At this point, both, cicero and caesar were persisted:
        cicero.getReservation().setStartDate(caesar.getReservation().getStartDate());
        cicero.getReservation().setEndDate(caesar.getReservation().getEndDate());
        ResponseEntity<String> result = doPost(cicero.getReservation(), MODIFY_ENDPOINT);
        String json = result.getBody();

        // 3 - Assert error message.
        assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
        assertThat(json, isJson());
        assertThat(json, hasJsonPath("$.message", equalTo("There has been an error while processing the request.")));
        assertThat(json, hasJsonPath("$.cause", equalTo("There exists reservations in that period. Please check availability.")));
        assertThat(json, hasJsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));

        // 4 - Assert that reservation has not been modified.
        User persisted = repository.findByReservationId(cicero.getReservation().getId());
        assertTrue(areEqualDates(persisted.getReservation().getStartDate(), r1.getStartDate())); // r1 is the original reserv. for user cicero.
        assertTrue(areEqualDates(persisted.getReservation().getEndDate(), r1.getEndDate()));
    }

    private boolean areEqualDates(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        return c1.compareTo(c2)==0;
    }


}
