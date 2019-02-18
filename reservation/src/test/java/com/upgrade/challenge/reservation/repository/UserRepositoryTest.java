package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.AbstractTestCase;
import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest extends AbstractTestCase {

    @Test
    public void givenUserWithCorrectData_whenUserSaves_thenUserIsPersisted() {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Reservation reservation = getReservation(getDate(d1.plusDays(1)), getDate(d1.plusDays(3)), null);
        User user = getUser("Gaivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        User persisted = userRepository.saveAndFlush(user);
        assertEquals(user, persisted);
        assertThat(persisted.getReservation(), hasProperty("id", greaterThan(0l)));
    }

    @Test
    public void givenUserWithNullData_whenUserSaves_thenAnExceptionisThrown() {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Name cannot be null"));
        exceptionRule.expectMessage(containsString("Last name cannot be null"));
        exceptionRule.expectMessage(containsString("Email cannot be null"));
        exceptionRule.expectMessage(containsString("'must not be null', propertyPath=reservation"));

        userRepository.saveAndFlush(getUser(null, null, null, null));
    }

    @Test
    public void givenUserWithoutFirstName_whenUserSaves_thenAnExceptionisThrown() {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Name cannot be null"));
        exceptionRule.expectMessage(containsString("Name must have 1 to 20 characters"));

        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Reservation reservation = getReservation(getDate(d1.plusDays(1)), getDate(d1.plusDays(3)), null);
        User user = getUser("", "Caesar", "gaivs.caesar@gmail.com", reservation);
        userRepository.saveAndFlush(user);
    }

    @Test
    public void givenUserWithoutLastName_whenUserSaves_thenAnExceptionisThrown() {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Last name cannot be null"));
        exceptionRule.expectMessage(containsString("Last name must have 1 to 20 characters"));

        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Reservation reservation = getReservation(getDate(d1.plusDays(1)), getDate(d1.plusDays(3)), null);
        User user = getUser("Gaivs", "", "gaivs.caesar@gmail.com", reservation);
        userRepository.saveAndFlush(user);
    }

    @Test
    public void givenUserWithIncorrectReservationRange_whenUserSaves_thenAnExceptionIsThrown() {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Stays are from 1 to 3 days"));

        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Reservation reservation = getReservation(getDate(d1), getDate(d1.plusDays(4)), null);
        User user = getUser("Gaivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        userRepository.saveAndFlush(user);
    }

    @Test
    public void givenUserWithReservationWithEndDateBeforeStartDate_whenUserSaves_thenAnExceptionIsThrown() {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Invalid date"));
        exceptionRule.expectMessage(containsString("Start date must be set before end date"));

        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        Reservation reservation = getReservation(getDate(d1), getDate(d1.minusDays(2)), null);
        User user = getUser("Gaivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        userRepository.saveAndFlush(user);
    }

    @Test
    public void givenUserWithReservationWithStartDateEqualsEndDate_whenUserSaves_thenAnExceptionIsThrown() {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Invalid date"));
        exceptionRule.expectMessage(containsString("Start date must be set before end date"));

        Reservation reservation = getReservation(new Date(), new Date(), null);
        User user = getUser("Gaivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        userRepository.saveAndFlush(user);
    }

    @Test
    public void givenUserWithReservationWithNullStartDateAndEndDate_whenUserSaves_thenAnExceptionIsThrown() {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Invalid date"));
        exceptionRule.expectMessage(containsString("propertyPath=startDate"));

        Reservation reservation = getReservation(null, new Date(), null);
        User user = getUser("Gaivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        userRepository.saveAndFlush(user);
    }

    @Test
    public void givenUserWithReservationWithStartDateAndNullEndDate_whenUserSaves_thenAnExceptionIsThrown() {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Invalid date"));
        exceptionRule.expectMessage(containsString("propertyPath=endDate"));

        Reservation reservation = getReservation(new Date(), null, null);
        User user = getUser("Gaivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        userRepository.saveAndFlush(user);
    }
}
