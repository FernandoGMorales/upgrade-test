package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
public class UserControllerTest extends BaseController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Test
    public void givenUserWithValidData_whenSaveIsSuccessful_thenAJsonWithUserDataIsRetrieved()
            throws Exception {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser("Alexander", "The Great", "alexander.magnus@gmail.com", reservation);

        when(service.save(user)).thenReturn(user);
        String string1 = getDateAsString(start);
        String string2 = getDateAsString(end);
        String json = getJson(string1, string2);

        String result = mvc.perform(post("/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(result, containsString("\"firstName\":\"Alexander\""));
        assertThat(result, containsString("\"lastName\":\"The Great\""));
        assertThat(result, containsString("\"email\":\"alexander.magnus@gmail.com\""));
        assertThat(result, containsString(string1));
        assertThat(result, containsString(string2));
    }

    @Test
    public void givenUserWithValidData_whenSave_andThereIsReservationInThatPeriod_thenAJsonWithErrorDetailsIsRetrieved()
            throws Exception {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser("Alexander", "The Great", "alexander.magnus@gmail.com", reservation);
        String error = "There exists reservations in that period. Please check availability.";

        when(service.save(user)).thenThrow(new UserException(error));
        String string1 = getDateAsString(start);
        String string2 = getDateAsString(end);
        String json = getJson(string1, string2);

        String result = mvc.perform(post("/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(result, containsString(error));
    }

    @Test
    public void givenUserWithoutName_whenSave_thenAJsonWithErrorDetailsIsRetrieved()
            throws Exception {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Name cannot be null"));
        exceptionRule.expectMessage(containsString("Name must have 1 to 20 characters"));

        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Date start = getDate(d1.plusDays(1));
        Date end = getDate(d1.plusDays(3));
        Reservation reservation = getReservation(start, end, null);
        User user = getUser("", "", "alexander.magnus@gmail.com", reservation);

//        when(service.save(user)).thenThrow()

        String string1 = getDateAsString(start);
        String string2 = getDateAsString(end);
        String json = getJson(string1, string2);

//        mvc.perform(post("/users/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)).andExpect();
    }


    private String getJson(String d1, String d2) {
        return "{\"email\":\"alexander.magnus@gmail.com\",\"firstName\":\"Alexander\",\"lastName\":\"The Great\",\"reservation\":{\"startDate\":\"" +
                d1 + "\",\"" +
                d2 +"\":\"2019-02-21\"}}";
    }

}
