package com.upgrade.challenge.reservation.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by fernando on 09/02/19.
 */
@Entity
public class Campsite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(orphanRemoval=true)
    private List<Reservation> reservations;

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
