package com.upgrade.challenge.reservation.exception.format;

/**
 * Created by fernando on 16/02/19.
 */
public class ResponseMsg {

    private String message;
    private String cause;
    private int status;

    public ResponseMsg(String message, String cause, int status) {
        this.message = message;
        this.cause = cause;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }

    public int getStatus() {
        return status;
    }

}
