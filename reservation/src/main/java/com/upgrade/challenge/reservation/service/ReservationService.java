package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.exception.EntityNotFoundException;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.domain.Reservation;

import java.util.Date;

/**
 * A contract for {@link Reservation} management.
 *
 * Created by fernando on 10/02/19.
 */
public interface ReservationService {
    /**
     * Cancel the {@link Reservation}
     * @param id unique identifier of the {@link Reservation}
     * @return the {@link Reservation} deleted.
     * @throws ReservationException
     * @throws EntityNotFoundException
     */
    Reservation cancel(Long id) throws ReservationException, EntityNotFoundException;

    /**
     * Modifies a {@link Reservation}.
     * @param reservation the {@link Reservation} to be modified.
     * @return the persisted {@link Reservation}
     * @throws ReservationException
     * @throws EntityNotFoundException
     */
    Reservation modify(Reservation reservation) throws ReservationException, EntityNotFoundException;
}
