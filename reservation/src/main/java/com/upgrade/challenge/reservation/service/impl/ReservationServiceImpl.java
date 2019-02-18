package com.upgrade.challenge.reservation.service.impl;

import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.repository.UserRepository;
import com.upgrade.challenge.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by fernando on 10/02/19.
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private final static Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private final static String WARN_MESSAGE = "There has been an error while processing this request.";
    public static final String MISSING_RESERVATION_ID = "Missing reservation id.";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override @Transactional
    public void cancel(Long id) throws ReservationException {
        if(id==null) {
            throw new ReservationException(MISSING_RESERVATION_ID);
        }
        try {
            if(reservationRepository.existsById(id)) {
                userRepository.deleteByReservationId(id);
            }
            else {
                throw new ReservationException("There isn't any reservation with id=" + id + ".");
            }
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw e;
        }
    }

    @Override @Transactional
    public Reservation modify(Reservation reservation) throws ReservationException {
        Reservation modified = null;
        try {
            if(reservation.getId()==null) {
                throw new ReservationException(MISSING_RESERVATION_ID);
            }
            User user = userRepository.findByReservationId(reservation.getId());
            if(user!=null) {
                List<Reservation> list =
                        reservationRepository.findByRange(reservation.getStartDate(), reservation.getEndDate());
                if(list.isEmpty() || (list.size()==1 && list.get(0).equals(user.getReservation()))) {
                    user.getReservation().setStartDate(reservation.getStartDate());
                    user.getReservation().setEndDate(reservation.getEndDate());
                    userRepository.saveAndFlush(user);
                    modified = user.getReservation();
                }
                else {
                    throw new ReservationException("There exists reservations in that period. Please check availability.");
                }
            }

        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw e;
        }
        return modified;
    }

}
