package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by fernando on 09/02/19.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findByStartDate(Date startDate);

    Reservation findByEndDate(Date endDate);

    /**
     * Search for {@link Reservation} entities in the given range.
     * @param date1 start date.
     * @param date2 end date.
     * @return a {@link List} of {@link Reservation} entities.
     */
    @Query("select r from Reservation r " +
            "where r.startDate between ?1 and ?2 " +
            "or (r.endDate > ?1 and r.endDate < ?2) " +
            "order by r.startDate asc")
    List<Reservation> findByRange(Date date1, Date date2);
}
