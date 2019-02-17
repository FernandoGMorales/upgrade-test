package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.repository.AbstractTestCase;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest extends AbstractTestCase {

    @After
    public void clearDB() {
        userRepository.deleteAll();
    }

    @Test
    public void givenUserMakesReservation_whenAllDataAreCorrect_andThereIsAvailability_thenUserIsPersistedToDB() {
        LocalDate start = LocalDate.now().plusDays(1);
        Reservation reservation = getReservation(getDate(start), getDate(start.plusDays(2)), null);
        User user = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        User persisted = null;
        try {
            persisted = userService.save(user);
        } catch (UserException e) {
            fail();
        }
        assertEquals(user, persisted);
        assertThat(persisted.getReservation(), hasProperty("id", notNullValue()));
    }

    @Test
    public void givenUserMakesReservation_whenAllDataAreCorrect_andThereIsNotAvailability_thenAnExceptionIsThrown() throws UserException {
        exceptionRule.expect(UserException.class);
        exceptionRule.expect(hasProperty("msg", equalTo("There exists reservations in that period. Please check availability.")));

        LocalDate start1 = LocalDate.now().plusDays(1);
        Reservation reservation1 = getReservation(getDate(start1), getDate(start1.plusDays(2)), null);
        User user1 = getUser("Marcvs Tvlivs", "Cicero", "marcvs.cicero@gmail.com", reservation1);

        LocalDate start2 = LocalDate.now().plusDays(1);
        Reservation reservation2 = getReservation(getDate(start2), getDate(start2.plusDays(2)), null);
        User user2 = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation2);

        userService.save(user1);
        userService.save(user2);
    }

    @Test
    public void givenUserMakesReservation_whenFirstNameIsMissing_thenAnExceptionIsThrown() throws UserException {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Name cannot be null"));
        exceptionRule.expectMessage(containsString("Name must have 1 to 20 characters"));

        LocalDate start1 = LocalDate.now().plusDays(1);
        Reservation reservation1 = getReservation(getDate(start1), getDate(start1.plusDays(2)), null);
        User user1 = getUser("", "Cicero", "marcvs.cicero@gmail.com", reservation1);

        userService.save(user1);
    }

    @Test
    public void givenUserMakesReservation_whenLastNameIsMissing_thenAnExceptionIsThrown() throws UserException {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Last name cannot be null"));
        exceptionRule.expectMessage(containsString("Last name must have 1 to 20 characters"));

        LocalDate start1 = LocalDate.now().plusDays(1);
        Reservation reservation1 = getReservation(getDate(start1), getDate(start1.plusDays(2)), null);
        User user1 = getUser("Marcvs Tvlivs", "", "marcvs.cicero@gmail.com", reservation1);

        userService.save(user1);
    }

    @Test
    public void givenUserMakesReservation_whenEmailIsMissing_thenAnExceptionIsThrown() throws UserException {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Email cannot be null"));

        LocalDate start1 = LocalDate.now().plusDays(1);
        Reservation reservation1 = getReservation(getDate(start1), getDate(start1.plusDays(2)), null);
        User user1 = getUser("Marcus Tvlivs", "Cicero", "", reservation1);

        userService.save(user1);
    }

    @Test
    public void givenUserMakesReservation_whenReservationIsMissing_thenAnExceptionIsThrown() throws UserException {
        exceptionRule.expect(UserException.class);
        exceptionRule.expect(hasProperty("msg", equalTo("Reservation is missing.")));

        userService.save(getUser("Marcus Tvlivs", "Cicero", "marcvs.cicero@gmail.com", null));
    }

    @Test
    public void givenUserMakesReservation_whenReservationStarDateIsMissing_thenAnExceptionIsThrown() throws UserException {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("'Invalid date.', propertyPath=startDate"));

        Reservation reservation1 = getReservation(null, getDate(LocalDate.now().plusDays(2)), null);
        User user1 = getUser("Marcus Tvlivs", "Cicero", "", reservation1);

        userService.save(user1);
    }

    @Test
    public void givenUserMakesReservation_whenReservationEndDateIsMissing_thenAnExceptionIsThrown() throws UserException {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("'Invalid date.', propertyPath=endDate"));

        Reservation reservation1 = getReservation(getDate(LocalDate.now().plusDays(2)), null, null);
        User user1 = getUser("Marcus Tvlivs", "Cicero", "", reservation1);

        userService.save(user1);
    }

}
