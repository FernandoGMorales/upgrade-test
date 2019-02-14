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
    private final static String WARN_MESSAGE = "There has been an error while processing this request.";

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
            LOG.error(WARN_MESSAGE, e);
            throw new UserException(WARN_MESSAGE);
        }
        return user;
    }

    @Override
    public User findByLastName(String lastName) throws UserException {
        User user = null;
        try {
            user = userRepository.findByLastName(lastName);
        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new UserException(WARN_MESSAGE);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws UserException {
        User user = null;
        try {
            user = userRepository.findByEmail(email);
        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new UserException(WARN_MESSAGE);
        }
        return user;
    }

    @Override
    public List<User> findAll() throws UserException {
        List<User> users = null;
        try {
            users = userRepository.findAll();
        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new UserException(WARN_MESSAGE);
        }
        return users;
    }

    @Override
    public User save(User user) throws UserException {
        User persisted = null;
        try {
            persisted = userRepository.save(user);
        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new UserException(WARN_MESSAGE);
        }
        return persisted;
    }
}
