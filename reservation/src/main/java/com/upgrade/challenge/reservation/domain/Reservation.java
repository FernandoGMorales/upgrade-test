package com.upgrade.challenge.reservation.domain;

import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import com.upgrade.challenge.reservation.validation.ReservationConstraint;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
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

    @Column(unique = true)
    @DatePatternConstraint
    private Date startDate;

    @Column(unique = true)
    @DatePatternConstraint
    private Date endDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
