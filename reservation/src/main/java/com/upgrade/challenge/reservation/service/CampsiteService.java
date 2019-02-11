package com.upgrade.challenge.reservation.service;

import java.util.List;

public interface CampsiteService {

    List<?> rangesAvailable(String startDate, String endDate);

}
