package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by fernando on 17/02/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @Test
    public void givenUserWantsToRegister_whenAllDataAreCorrect_thenIsPersistedToDB() {
        User user = getUser(null, null, null, getReservation(new Date(), new Date(), null));
        Mockito.when(reservationRepository
                .findByRange(null, null)).thenReturn(new ArrayList<Reservation>());
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        try {
            assertEquals(service.save(user), user);
        } catch (UserException e) {
            fail();
        }
    }

    @Test
    public void givenUserWantsToRegister_whenDataAreMissing_thenAnExceptionIsThrown() {
        User user = getUser(null, null, null, getReservation(new Date(), new Date(), null));
        Mockito.when(reservationRepository
                .findByRange(null, null)).thenReturn(new ArrayList<Reservation>());
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        try {
            assertEquals(service.save(user), user);
        } catch (UserException e) {
            fail();
        }
    }

    private User getUser(String name, String lastname, String email, Reservation reservation) {
        User user = new User();
        user.setFirstName(name);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setReservation(reservation);
        return user;
    }

    private Reservation getReservation(Date startDate, Date endDate, Long id) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setId(id);
        return reservation;
    }

}
