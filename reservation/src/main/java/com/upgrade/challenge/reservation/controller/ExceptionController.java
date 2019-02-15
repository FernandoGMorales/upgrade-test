package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.exception.AppException;
import com.upgrade.challenge.reservation.exception.CampsiteException;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Created by fernando on 14/02/19.
 */
@RestControllerAdvice
public class ExceptionController {

    private static final String MESSAGE = "There has been an error while processing the request.";

    @ExceptionHandler(UserException.class)
    public ResponseMsg handleUserExceptions(UserException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), HttpStatus.SERVICE_UNAVAILABLE.value());
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseMsg handleReservationExceptions(ReservationException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), HttpStatus.SERVICE_UNAVAILABLE.value());
    }

    @ExceptionHandler(CampsiteException.class)
    public ResponseMsg handleCampsiteExceptions(CampsiteException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), HttpStatus.SERVICE_UNAVAILABLE.value());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseMsg handleMissingParams(MissingServletRequestParameterException ex) {
        return new ResponseMsg(MESSAGE, "Missing parameter: " + ex.getParameterName(), HttpStatus.SERVICE_UNAVAILABLE.value());
    }

    class ResponseMsg {

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

}
