package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.EntityNotFoundException;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.BaseTestCase;
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
public class ReservationServiceTest extends BaseTestCase {

    @After
    public void cleanDB() {
        userRepository.deleteAll();
    }

    @Test
    public void givenReservationId_whenCancel_andReservationExists_thenReservationIsDeletedFromDB() {
        LocalDate start = LocalDate.now().plusDays(1);
        Reservation reservation = getReservation(getDate(start), getDate(start.plusDays(2)), null);
        User user = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation);

        User persisted = null;
        try {
            persisted = userService.save(user);
            assertEquals(user, persisted);
            assertThat(persisted.getReservation(), hasProperty("id", notNullValue()));

            Long id = persisted.getReservation().getId();
            reservationService.cancel(id);

            assertThat(reservationRepository.findById(id).isPresent(), is(false));
        } catch (UserException | ReservationException | EntityNotFoundException e) {
            fail();
        }
    }

    @Test
    public void givenReservationId_whenCancel_andReservationNotExists_thenAnExceptionIsThrown()
            throws ReservationException, EntityNotFoundException {
        Long id = 1000l;
        exceptionRule.expect(ReservationException.class);
        exceptionRule.expect(hasProperty("msg", equalTo("There isn't any reservation with id=" + id + ".")));

        reservationService.cancel(id);
    }

    @Test
    public void givenNullId_whenCancel_thenAnExceptionIsThrown() throws ReservationException, EntityNotFoundException {
        exceptionRule.expect(ReservationException.class);
        exceptionRule.expect(hasProperty("msg", equalTo("Missing reservation id.")));

        reservationService.cancel(null);
    }

    @Test
    public void givenValidReservation_whenModify_andReservationExists_andThereAreNotOtherReservationsInThatDateRange_thenReservationIsUpdated() {
        LocalDate start = LocalDate.now().plusDays(1);
        Reservation reservation = getReservation(getDate(start), getDate(start.plusDays(2)), null);
        User user = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation);

        User persisted = null;
        try {
            persisted = userService.save(user);
            assertEquals(user, persisted);
            assertThat(persisted.getReservation(), hasProperty("id", notNullValue()));

            start = start.plusDays(5);
            reservation = getReservation(getDate(start), getDate(start.plusDays(3)), persisted.getReservation().getId());
            Reservation modified = reservationService.modify(reservation);

            assertEquals(reservation, modified);
        } catch (UserException | ReservationException | EntityNotFoundException e) {
            fail();
        }
    }

    @Test
    public void givenValidReservation_whenModify_andReservationExists_andThereAreOtherReservationsInThatDateRange_thenAnExceptionIsThrown()
            throws ReservationException, EntityNotFoundException, UserException {

        exceptionRule.expect(ReservationException.class);
        exceptionRule.expect(hasProperty("msg", equalTo("There exists reservations in that period. Please check availability.")));

        LocalDate start = LocalDate.now().plusDays(1);
        Reservation reservation1 = getReservation(getDate(start), getDate(start.plusDays(2)), null);
        User user1 = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation1);

        start = start.plusDays(3);
        Reservation reservation2 = getReservation(getDate(start), getDate(start.plusDays(1)), null);
        User user2 = getUser("Marcvs Tvlivs", "Cicero", "marcvs.cicero@gmail.com", reservation2);

        User persisted1 = null;
        User persisted2 = null;

        persisted1 = userService.save(user1);
        assertEquals(user1, persisted1);
        assertThat(persisted1.getReservation(), hasProperty("id", notNullValue()));

        persisted2 = userService.save(user2);
        assertEquals(user2, persisted2);
        assertThat(persisted2.getReservation(), hasProperty("id", notNullValue()));

        // modify user2 reservation, so that it overlaps with user1 reservation's
        reservation2 = persisted2.getReservation();
        reservation2.setStartDate(reservation1.getStartDate());
        reservation2.setEndDate(reservation1.getEndDate());
        reservationService.modify(reservation2);
    }

    @Test
    public void givenReservationWithInvalidDateRange_whenModify_thenAnExceptionIsThrown()
            throws UserException, ReservationException, EntityNotFoundException {

        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("Start date must be set before end date"));
        exceptionRule.expectMessage(containsString("must be a future date', propertyPath=endDate"));
        exceptionRule.expectMessage(containsString("Stays are from 1 to 3 days."));
        exceptionRule.expectMessage(containsString("Reservations in advance must be placed 1 to 30 days prior to arrive"));

        LocalDate start = LocalDate.now().plusDays(1);
        Reservation reservation1 = getReservation(getDate(start), getDate(start.plusDays(2)), null);
        User user1 = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation1);

        User persisted1 = null;
        persisted1 = userService.save(user1);
        assertEquals(user1, persisted1);
        assertThat(persisted1.getReservation(), hasProperty("id", notNullValue()));

        reservation1.setStartDate(getDate(start.plusMonths(2)));
        reservation1.setEndDate(getDate(start.minusMonths(3)));
        reservation1.setId(persisted1.getReservation().getId());
        reservationService.modify(reservation1);
    }

    @Test
    public void givenReservationWithNullDateRange_whenModify_thenAnExceptionIsThrown() throws UserException, ReservationException, EntityNotFoundException {
        exceptionRule.expect(ConstraintViolationException.class);
        exceptionRule.expectMessage(containsString("'Invalid date.', propertyPath=startDate"));
        exceptionRule.expectMessage(containsString("'Invalid date.', propertyPath=endDate"));

        LocalDate start = LocalDate.now().plusDays(1);
        Reservation reservation1 = getReservation(getDate(start), getDate(start.plusDays(2)), null);
        User user1 = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation1);

        User persisted1 = null;
        persisted1 = userService.save(user1);
        assertEquals(user1, persisted1);
        assertThat(persisted1.getReservation(), hasProperty("id", notNullValue()));

        reservationService.modify(getReservation(null, null, persisted1.getReservation().getId()));
    }

}
