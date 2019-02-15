package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.controller.dto.Range;
import com.upgrade.challenge.reservation.exception.CampsiteException;

import java.util.Date;
import java.util.List;

public interface CampsiteService {

    List<Range> findByRange(Date date1, Date date2) throws CampsiteException;

}
