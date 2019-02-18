package com.upgrade.challenge.reservation.repository;

import com.upgrade.challenge.reservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fernando on 09/02/19.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByReservationId(Long id);
    User findByReservationId(Long id);
}
