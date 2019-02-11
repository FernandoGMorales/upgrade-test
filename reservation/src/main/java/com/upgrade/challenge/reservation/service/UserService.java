package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.domain.User;

/**
 * A contract for {@link User} management.
 *
 * Created by fernando on 10/02/19.
 */

public interface UserService {
    /**
     * Search for {@link User} with the given name.
     * @param firstName user's name.
     * @return the {@link User} found, otherwise null.
     * @throws UserException
     */
    User findByFirstName(String firstName) throws UserException;

    /**
     * Search for {@link User} with the given last name.
     * @param lastName user's last name.
     * @return the {@link User} found, otherwise null.
     * @throws UserException
     */
    User findByLastName(String lastName) throws UserException;

    /**
     * Search for {@link User} with the given email.
     * @param email user's email.
     * @return the {@link User} found, otherwise null.
     * @throws UserException
     */
    User findByEmail(String email) throws UserException;

    /**
     * Persist a {@link User}
     * @param user the {@link User} to be saved.
     * @return the persisted {@link User}
     * @throws UserException
     */
    User save(User user) throws UserException;
}
