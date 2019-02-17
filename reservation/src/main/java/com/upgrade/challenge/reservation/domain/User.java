package com.upgrade.challenge.reservation.domain;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Created by fernando on 09/02/19.
 */
@Entity
@IdClass(PersonId.class)
@Validated
public class User {

    @NotEmpty(message = "Email cannot be null.")
    @NotNull
    @Email(message = "Email should be valid.")
    @Id private String email;

    @NotEmpty(message = "Name cannot be null.")
    @NotNull
    @Size(min = 1, max = 20, message = "Name must have 1 to 20 characters.")
    @Id private String firstName;

    @NotEmpty(message = "Last name cannot be null")
    @NotNull
    @Size(min = 1, max = 20, message = "Last name must have 1 to 20 characters.")
    @Id private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id")
    @NotNull
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

    @Override
    public int hashCode() {
        return firstName.hashCode() * lastName.hashCode() * email.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User))
            return false;
        User user = (User) obj;
        return this.firstName.equals(user.getFirstName()) &&
                this.lastName.equals(user.getLastName()) &&
                this.email.equals(user.getEmail());
    }
}
