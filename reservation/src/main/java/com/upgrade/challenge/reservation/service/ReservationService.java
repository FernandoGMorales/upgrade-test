package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.domain.Reservation;

/**
 * A contract for {@link Reservation} management.
 *
 * Created by fernando on 10/02/19.
 */
public interface ReservationService {
    /**
     * Search for {@link Reservation} in the given date.
     * @param startDate the target date.
     * @return the {@link Reservation} found, otherwise null.
     * @throws ReservationException
     */
    Reservation findByStartDate(String startDate) throws ReservationException;

    /**
     * Search for {@link Reservation} in the given date.
     * @param endDate the target date.
     * @return the {@link Reservation} found, otherwise null.
     * @throws ReservationException
     */
    Reservation findByEndDate(String endDate) throws ReservationException;

    /**
     * Persist a {@link Reservation}.
     * @param reservation the {@link Reservation} to be saved.
     * @return the persisted {@link Reservation}
     * @throws ReservationException
     */
    Reservation save(Reservation reservation) throws ReservationException;

    /**
     * Cancel the {@link Reservation}
     * @param id unique identifier of the {@link Reservation}
     * @throws ReservationException
     */
    void cancel(Long id) throws ReservationException;

    /**
     * Modifies a {@link Reservation}.
     * @param reservation the {@link Reservation} to be modified.
     * @return the persisted {@link Reservation}
     * @throws ReservationException
     */
    Reservation modify(Reservation reservation) throws ReservationException;



}
