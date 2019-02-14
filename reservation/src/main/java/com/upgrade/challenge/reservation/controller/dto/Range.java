package com.upgrade.challenge.reservation.controller.dto;

/**
 * Created by fernando on 13/02/19.
 */
public class Range {

    private String fromDate;
    private String toDate;

    public Range(String date1, String date2) {
        this.fromDate = date1;
        this.toDate = date2;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

}
