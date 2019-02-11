package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fernando on 09/02/19.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByStartDate(String fromDate);
    Reservation findByEndDate(String toDate);
}
