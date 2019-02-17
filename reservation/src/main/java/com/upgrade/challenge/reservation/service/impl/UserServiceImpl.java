package com.upgrade.challenge.reservation.service.impl;

import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.exception.EntityNotFoundException;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.repository.UserRepository;
import com.upgrade.challenge.reservation.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created by fernando on 10/02/19.
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final static String ERROR = "Cannot perform request call: ";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<User> findAll() throws UserException, EntityNotFoundException {
        List<User> users = null;
        try {
            users = userRepository.findAll();
            if(users.isEmpty())
                throw new EntityNotFoundException("No users found!");
        } catch(Exception e) {
            handleError(e);
        }
        return users;
    }

    @Override @Transactional
    public User save(User user) throws UserException {
        User persisted = null;
        try {
            Reservation reservation = user.getReservation();
            if(reservation==null) {
                throw new UserException("Reservation is missing.");
            }
            List<Reservation> reservations = reservationRepository.
                    findByRange(reservation.getStartDate(), reservation.getEndDate());
            if(reservations.isEmpty()) {
                persisted = userRepository.saveAndFlush(user);
            }
            else {
                throw new UserException("There exists reservations in that period. Please check availability.");
            }
        } catch(Exception e) {
            throw e;
        }
        return persisted;
    }

    private void handleError(Exception e) throws EntityNotFoundException, UserException {
        if(e instanceof EntityNotFoundException)
            throw (EntityNotFoundException) e;
        else
            throw new UserException(ERROR + e.getCause().getMessage());
    }

}
