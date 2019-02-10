package com.upgrade.challenge.reservation.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by fernando on 09/02/19.
 */
@Entity
public class Reservation {

    enum Constraints {
        COST(0),
        MAX_DAYS_TO_RESERVE(3),
        MIN_DAYS_TO_DO_RESERVATION(1),
        MAX_DAYS_TO_DO_RESERVATION(30),
        CHECK_IN_HOUR(12),
        CHECK_OUT_HOUR(12);

        private int units;

        Constraints(int units) {
            this.units = units;
        }

        public int getUnits() {
            return units;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private Date fromDate;
    private Date toDate;
    private int hour;
    @OneToOne(mappedBy = "reservation")
    private User user;

    public Long getId() {
        return id;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
