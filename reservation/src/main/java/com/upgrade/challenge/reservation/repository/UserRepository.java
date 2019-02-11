package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fernando on 09/02/19.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstName(String firstName);
    User findByLastName(String lastName);
    User findByEmail(String email);
}
