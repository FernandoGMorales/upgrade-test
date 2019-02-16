package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.exception.CampsiteException;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.exception.format.ResponseMsg;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


/**
 * Created by fernando on 14/02/19.
 */
@RestControllerAdvice
public class ExceptionController {

    private static final String MESSAGE = "There has been an error while processing the request.";
    private static final int STATUS = HttpStatus.BAD_REQUEST.value();

    @ExceptionHandler(UserException.class)
    public ResponseMsg handleUserExceptions(UserException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), STATUS);
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseMsg handleReservationExceptions(ReservationException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), STATUS);
    }

    @ExceptionHandler(CampsiteException.class)
    public ResponseMsg handleCampsiteExceptions(CampsiteException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), STATUS);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseMsg handleMissingParams(MissingServletRequestParameterException ex) {
        return new ResponseMsg(MESSAGE, "Missing parameter: " + ex.getParameterName(), STATUS);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseMsg handleTransactionExceptions(TransactionSystemException ex) {
        Throwable t = ex.getRootCause();
        StringBuilder errors = new StringBuilder();
        if(t instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) t;
            for(ConstraintViolation cv : cve.getConstraintViolations()) {
                errors.append(cv.getMessage()).append(System.lineSeparator());
            }
        }
        else {
            errors.append(t.getMessage());
        }
        return new ResponseMsg(MESSAGE, errors.toString(), STATUS);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseMsg handleUnsupportedMethod(HttpRequestMethodNotSupportedException ex) {
        return new ResponseMsg(MESSAGE, ex.getMethod() + " request not supported.", STATUS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseMsg handleAll(Exception ex) {
        return new ResponseMsg(MESSAGE, ex.getMessage(), STATUS);
    }

}
