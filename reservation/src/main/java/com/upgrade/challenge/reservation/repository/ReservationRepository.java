package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

/**
 * Created by fernando on 09/02/19.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findByStartDate(String startDate);

    Reservation findByEndDate(String endDate);

    /**
     * Search for {@link Reservation} entities in the given range.
     * @param date1 start date.
     * @param date2 end date.
     * @return a {@link Stream} of {@link Reservation} entities.
     */
    @Query("select r from Reservation r " +
            "where r.startDate between :date1 and :date2 " +
            "or (r.endDate > :date1 and r.endDate < :date2) " +
            "order by r.startDate asc")
    Stream<Reservation> findByRange(String date1, String date2);
}
