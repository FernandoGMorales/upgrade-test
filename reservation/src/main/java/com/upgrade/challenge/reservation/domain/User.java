package com.upgrade.challenge.reservation.domain;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created by fernando on 09/02/19.
 */
@Entity
@Validated
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Email cannot be null.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotEmpty(message = "Name cannot be null.")
    private String firstName;

    @NotEmpty(message = "Last name cannot be null")
    private String lastName;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Reservation reservation;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
