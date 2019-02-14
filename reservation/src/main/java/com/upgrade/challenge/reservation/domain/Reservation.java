package com.upgrade.challenge.reservation.domain;

import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import com.upgrade.challenge.reservation.validation.ReservationConstraint;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by fernando on 09/02/19.
 */
@Entity
@Validated
@ReservationConstraint(
        startDate = "startDate",
        endDate = "endDate",
        message = "Date range is invalid!"
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @DatePatternConstraint
    private Date startDate;

    @DatePatternConstraint
    private Date endDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        return this.startDate.hashCode() * this.endDate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Reservation))
            return false;
        Reservation r = (Reservation) obj;
        return this.startDate.equals(r.getStartDate()) && this.endDate.equals(r.getEndDate());
    }
}
