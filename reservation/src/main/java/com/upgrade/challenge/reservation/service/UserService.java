package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.exception.EntityNotFoundException;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.domain.User;

import java.util.List;

/**
 * A contract for {@link User} management.
 *
 * Created by fernando on 10/02/19.
 */

public interface UserService {
    /**
     * Find all {@link User}
     * @return a {@link List} of all {@link User}
     * @throws UserException
     * @throws EntityNotFoundException
     */
    List<User> findAll() throws UserException, EntityNotFoundException;

    /**
     * Persist a {@link User}
     * @param user the {@link User} to be saved.
     * @return the persisted {@link User}
     * @throws UserException
     */
    User save(User user) throws UserException;
}
