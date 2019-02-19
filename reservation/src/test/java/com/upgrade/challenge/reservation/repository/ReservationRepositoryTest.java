package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.BaseTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationRepositoryTest extends BaseTestCase {

    @Before
    public void init() {
        initDataset();
    }

    @After
    public void clearDB() {
        userRepository.deleteAll();
    }

    @Test
    public void givenValidRange_whenCallFindByRange_andAllReservationsAreInRange_thenAListOfAllReservationsIsRetrieved() {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        assertThat(reservationRepository.findByRange(getDate(d1), getDate(d1.plusMonths(1))), hasSize(6));
    }

    @Test
    public void givenValidRange_whenCallFindByRange_andSomeReservationsAreInRange_thenAListOfThoseReservationsIsRetrieved() {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date()));
        assertThat(reservationRepository.findByRange(getDate(d1), getDate(d1.plusDays(10))), hasSize(3));
    }

    @Test
    public void givenValidRange_whenCallFindByRange_andNoneReservationsAreInRange_thenAnEmptyListIsRetrieved() {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date())).minusDays(3);
        assertThat(reservationRepository.findByRange(getDate(d1), getDate(d1.plusDays(1))), hasSize(0));
    }

    @Test
    public void givenAnInValidRange_whenCallFindByRange_thenAnEmptyListIsRetrieved() {
        LocalDate d1 = LocalDate.parse(getDateAsString(new Date())).plusMonths(1);
        assertThat(reservationRepository.findByRange(getDate(d1), getDate(LocalDate.now())), hasSize(0));
    }

    @Test
    public void givenNullDateRanges_whenCallFindByRange_thenAnEmptyListIsRetrieved() {
        assertThat(reservationRepository.findByRange(null, null), hasSize(0));
    }
}
