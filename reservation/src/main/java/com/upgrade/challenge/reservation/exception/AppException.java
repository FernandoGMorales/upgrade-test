package com.upgrade.challenge.reservation.exception;

/**
 * Created by fernando on 10/02/19.
 */
abstract class AppException extends Exception {

    private final String msg;

    public AppException(String msg) {
        super();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
