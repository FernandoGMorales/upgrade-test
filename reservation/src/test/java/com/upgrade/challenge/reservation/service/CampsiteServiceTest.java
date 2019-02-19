package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.controller.dto.Range;
import com.upgrade.challenge.reservation.exception.CampsiteException;
import com.upgrade.challenge.reservation.BaseTestCase;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CampsiteServiceTest extends BaseTestCase {

    @Before
    public void init() {
        initDataset();
    }

    @After
    public void clearDB() {
        userRepository.deleteAll();
    }

    @Test
    public void givenValidRange_whenFindByRange_andThereAreReservationsInRange_thenAListWithThoseReservationsIsRetrieved() {
        List<Range> list;
        LocalDate start = LocalDate.now().plusDays(2);
        try {
            list = campsiteService.findByRange(getDate(start), getDate(start.plusMonths(1)));
            assertThat(list.size(), equalTo(6));
        } catch (CampsiteException e) {
            fail();
        }
    }

    @Test
    public void givenValidRange_whenFindByRange_andThereAreNotReservationsInRange_thenAListWithThatRangeIsRetrieved() {
        List<Range> list;
        LocalDate start = LocalDate.now().plusMonths(1);
        Date d1 = getDate(start);
        Date d2 = getDate(start.plusDays(10));
        Range range = new Range(
                DatePatternConstraint.DATE_FORMAT.format(d1),
                DatePatternConstraint.DATE_FORMAT.format(d2)
        );
        try {
            list = campsiteService.findByRange(d1, d2);
            assertThat(list.size(), equalTo(1));
            assertEquals(range, list.get(0));
        } catch (CampsiteException e) {
            fail();
        }
    }

}
