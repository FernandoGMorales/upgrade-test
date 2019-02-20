package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.controller.dto.Range;
import com.upgrade.challenge.reservation.exception.CampsiteException;
import com.upgrade.challenge.reservation.service.UserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


/**
 * Created by fernando on 17/02/19.
 */
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
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.*;


/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampsiteControllerTest extends BaseController {

    @Autowired
    private CampsiteController controller;

    @Test
    public void givenValidDates_whenCheckForAvailability_thenJsonWithAvailableRangesIsRetrieved() throws CampsiteException {
        initDataset();

        String startDate = DatePatternConstraint.DATE_FORMAT.format(getDate(LocalDate.now()));
        String endDate = DatePatternConstraint.DATE_FORMAT.format(getDate(LocalDate.now().plusMonths(1)));
        List<Range> ranges = controller.findByRange(startDate, endDate);
        List<Reservation> reservations = reservationRepository.findAll();

        assertNotNull(ranges);
        assertNotNull(reservations);
        assertThat(ranges.size(), is(greaterThan(reservations.size())));
    }

    /*
        This data set has reservations in the range start = tomorrow, end = tomorrow + 22 days.
     */
    protected void initDataset() {
        LocalDate start = LocalDate.parse(getDateAsString(new Date())).plusDays(1);
        LocalDate end = start.plusDays(3);

        Reservation reservation = getReservation(getDate(start), getDate(end), null);
        User user = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        repository.saveAndFlush(user);

        start = end.plusDays(1);
        end = start.plusDays(1);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Marcvs Tvlivs", "Cicero", "marcvs.cicero@gmail.com", reservation);
        repository.saveAndFlush(user);

        start = end.plusDays(2);
        end = start.plusDays(2);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Antigonus I", "Monophthalmus", "antigonus.monophthalmus@gmail.com", reservation);
        repository.saveAndFlush(user);

        start = end.plusDays(5);
        end = start.plusDays(3);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Eumenes", "Cardia", "eumenes.cardia@gmail.com", reservation);
        repository.saveAndFlush(user);

        start = end.plusDays(1);
        end = start.plusDays(1);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Alexander", "The Great", "alexander.thegreat@gmail.com", reservation);
        repository.saveAndFlush(user);

        start = end.plusDays(2);
        end = start.plusDays(1);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Cyrus", "The Great", "cyrus.thegreat@gmail.com", reservation);
        repository.saveAndFlush(user);
    }

}
