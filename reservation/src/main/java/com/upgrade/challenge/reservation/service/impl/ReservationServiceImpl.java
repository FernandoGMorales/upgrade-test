package com.upgrade.challenge.reservation.service.impl;

import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.domain.Reservation;
import com.upgrade.challenge.reservation.repository.ReservationRepository;
import com.upgrade.challenge.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
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
    public Reservation findByStartDate(Date startDate) throws ReservationException {
        Reservation reservation = null;
        try {
            reservation = repository.findByStartDate(startDate);
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new ReservationException(WARN_MESSAGE);
        }
        return reservation;
    }

    @Override
    public Reservation findByEndDate(Date endDate) throws ReservationException {
        Reservation reservation = null;
        try {
            reservation = repository.findByEndDate(endDate);
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new ReservationException(WARN_MESSAGE);
        }
        return reservation;
    }

    @Override
    @Transactional
    public Reservation save(Reservation reservation) throws ReservationException {
        Reservation persisted = null;
        try {
            persisted = repository.save(reservation);
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new ReservationException(WARN_MESSAGE);
        }
        return persisted;
    }

    @Override
    public void cancel(Long id) throws ReservationException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new ReservationException(WARN_MESSAGE);
        }
    }

    @Override
    public Reservation modify(Reservation reservation) throws ReservationException {
        Reservation modified = null;
        try {
            Reservation persisted = repository.findById(reservation.getId()).get();
            persisted.setStartDate(reservation.getStartDate());
            persisted.setEndDate(reservation.getEndDate());
            modified = this.save(persisted);
        } catch(Exception e) {
            LOG.error(WARN_MESSAGE, e);
            throw new ReservationException(WARN_MESSAGE);
        }
        return modified;
    }

}
