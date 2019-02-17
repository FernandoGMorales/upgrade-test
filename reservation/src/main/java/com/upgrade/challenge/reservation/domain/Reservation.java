package com.upgrade.challenge.reservation.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import com.upgrade.challenge.reservation.validation.ReservationConstraint;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.util.Date;

/**
 * Created by fernando on 09/02/19.
 */
@Entity
@Validated
@ReservationConstraint(
        startDate = "startDate",
        endDate = "endDate",
        message = "Reservations in advance must be placed 1 to 30 days prior to arrive.\n" +
                "Stays are from 1 to 3 days.\n" +
                "Start date must be set before end date.\n" +
                "Date pattern must be yyyy-MM-dd"
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @DatePatternConstraint
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(unique=true)
    private Date startDate;

    @DatePatternConstraint
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(unique=true)
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
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Reservation))
            return false;
        Reservation r = (Reservation) obj;
        return this.id.equals(r.getId());
    }
}
