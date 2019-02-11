package com.upgrade.challenge.reservation.domain;

import com.upgrade.challenge.reservation.validation.DatePatternConstraint;
import com.upgrade.challenge.reservation.validation.ReservationConstraint;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

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
    private String startDate;

    @Column(unique = true)
    @DatePatternConstraint
    private String endDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
