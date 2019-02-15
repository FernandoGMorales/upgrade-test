package com.upgrade.challenge.reservation.service.impl;

import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.repository.UserRepository;
import com.upgrade.challenge.reservation.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fernando on 10/02/19.
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public User findByFirstName(String firstName) throws UserException {
        User user = null;
        try {
            user = userRepository.findByFirstName(firstName);
        } catch(Exception e) {
            handleException(e, "User with first name " + firstName + " has not been found!");
        }
        return user;
    }

    @Override
    public User findByLastName(String lastName) throws UserException {
        User user = null;
        try {
            user = userRepository.findByLastName(lastName);
        } catch(Exception e) {
            handleException(e, "User with last name " + lastName + " has not been found!");
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws UserException {
        User user = null;
        try {
            user = userRepository.findByEmail(email);
        } catch(Exception e) {
            handleException(e, "User with email " + email + " has not been found!");
        }
        return user;
    }

    @Override
    public List<User> findAll() throws UserException {
        List<User> users = null;
        try {
            users = userRepository.findAll();
        } catch(Exception e) {
            handleException(e, "There are no users found!");
        }
        return users;
    }

    @Override
    public User save(User user) throws UserException {
        User persisted = null;
        try {
            persisted = userRepository.save(user);
        } catch(Exception e) {
            handleException(e, "The user cannot be persisted!");
        }
        return persisted;
    }

    private void handleException(Exception e, String message) throws UserException {
        LOG.error(message, e);
        throw new UserException(message);
    }

}
