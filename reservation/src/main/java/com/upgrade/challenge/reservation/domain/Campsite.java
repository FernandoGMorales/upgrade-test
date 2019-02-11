package com.upgrade.challenge.reservation.domain;

import javax.persistence.Embeddable;
import java.util.List;

/**
 * Created by fernando on 11/02/19.
 */
public class Campsite {

    private List<Range> availability;

    private class Range {
        private String date1;
        private String date2;

        public Range(String date1, String date2) {
            this.date1 = date1;
            this.date2 = date2;
        }
    }
}
