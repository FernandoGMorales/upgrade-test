package com.upgrade.challenge.reservation.service.impl;

import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by fernando on 10/02/19.
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private final static Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private final static String WARN_MESSAGE = "There has been an error while processing this request.";

    @Autowired
    private ReservationRepository repository;

    @Override
    public void cancel(Long id) throws ReservationException {
        try {
            if(repository.findById(id).isPresent()) {
                repository.deleteById(id);
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
            List<Reservation> list = repository.findByRange(reservation.getStartDate(), reservation.getEndDate());
            if(list.isEmpty() || (list.size()==1 && list.get(0).equals(reservation))) {
                Reservation persisted = list.get(0);
                persisted.setStartDate(reservation.getStartDate());
                persisted.setEndDate(reservation.getEndDate());
                modified = repository.saveAndFlush(persisted);
            }
            else {
                throw new ReservationException("There exists reservations in that period. Please check availability.");
            }
        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw e;
        }
        return modified;
    }

}
