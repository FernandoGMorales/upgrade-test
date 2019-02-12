package com.upgrade.challenge.reservation.service;

import java.util.List;

public interface CampsiteService {

    List<?> findByRange(String date1, String date2);

}
