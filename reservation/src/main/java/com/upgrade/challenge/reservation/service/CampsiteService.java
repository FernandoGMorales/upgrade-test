package com.upgrade.challenge.reservation.service;

import com.upgrade.challenge.reservation.controller.dto.Range;

import java.util.Date;
import java.util.List;

public interface CampsiteService {

    List<Range> findByRange(Date date1, Date date2);

}
