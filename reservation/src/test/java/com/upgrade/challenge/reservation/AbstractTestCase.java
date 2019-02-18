package com.upgrade.challenge.reservation;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.repository.UserRepository;
import com.upgrade.challenge.reservation.service.CampsiteService;
import com.upgrade.challenge.reservation.service.ReservationService;
import com.upgrade.challenge.reservation.service.UserService;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by fernando on 17/02/19.
 */
public abstract class AbstractTestCase {

    @Autowired
    protected ReservationRepository reservationRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ReservationService reservationService;

    @Autowired
    protected CampsiteService campsiteService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    protected CharSequence getDateAsString(Date date) {
        return DatePatternConstraint.DATE_FORMAT.format(date);
    }

    protected Date getDate(LocalDate d1) {
        return Date.from(d1.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    protected User getUser(String name, String lastname, String email, Reservation reservation) {
        User user = new User();
        user.setFirstName(name);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setReservation(reservation);
        return user;
    }

    protected Reservation getReservation(Date startDate, Date endDate, Long id) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setId(id);
        return reservation;
    }

    protected void initDataset() {
        LocalDate start = LocalDate.parse(getDateAsString(new Date())).plusDays(1);
        LocalDate end = start.plusDays(3);

        Reservation reservation = getReservation(getDate(start), getDate(end), null);
        User user = getUser("Gaivs Ivlivs", "Caesar", "gaivs.caesar@gmail.com", reservation);
        userRepository.saveAndFlush(user);

        start = end.plusDays(1);
        end = start.plusDays(1);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Marcvs Tvlivs", "Cicero", "marcvs.cicero@gmail.com", reservation);
        userRepository.saveAndFlush(user);

        start = end.plusDays(2);
        end = start.plusDays(2);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Antigonus I", "Monophthalmus", "antigonus.monophthalmus@gmail.com", reservation);
        userRepository.saveAndFlush(user);

        start = end.plusDays(5);
        end = start.plusDays(3);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Eumenes", "Cardia", "eumenes.cardia@gmail.com", reservation);
        userRepository.saveAndFlush(user);

        start = end.plusDays(1);
        end = start.plusDays(1);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Alexander", "The Great", "alexander.thegreat@gmail.com", reservation);
        userRepository.saveAndFlush(user);

        start = end.plusDays(2);
        end = start.plusDays(1);
        reservation = getReservation(getDate(start), getDate(end), null);
        user = getUser("Cyrus", "The Great", "cyrus.thegreat@gmail.com", reservation);
        userRepository.saveAndFlush(user);
    }

}
