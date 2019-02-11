package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

/**
 * Created by fernando on 09/02/19.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByStartDate(String startDate);
    Reservation findByEndDate(String endDate);
    Stream<Reservation> findByStartDateOrEndDateOrderByStartDate(String startDate, String endDate);
}
