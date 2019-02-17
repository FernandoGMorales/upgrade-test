package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.exception.CampsiteException;
import com.upgrade.challenge.reservation.exception.EntityNotFoundException;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.exception.format.ResponseMsg;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


/**
 * Created by fernando on 14/02/19.
 */
@RestControllerAdvice
public class ExceptionController {

    private static final String MESSAGE = "There has been an error while processing the request.";
    private static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();
    private static final int NOT_FOUND = HttpStatus.NOT_FOUND.value();

    @ExceptionHandler(UserException.class)
    public ResponseMsg handleUserExceptions(UserException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), BAD_REQUEST);
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseMsg handleReservationExceptions(ReservationException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), BAD_REQUEST);
    }

    @ExceptionHandler(CampsiteException.class)
    public ResponseMsg handleCampsiteExceptions(CampsiteException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseMsg handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseMsg(MESSAGE, ex.getMsg(), NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseMsg handleMissingParams(MissingServletRequestParameterException ex) {
        return new ResponseMsg(MESSAGE, "Missing parameter: " + ex.getParameterName(), BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseMsg handleValidationsViolations(TransactionSystemException ex) {
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
        return new ResponseMsg(MESSAGE, errors.toString(), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseMsg handleConstraintExceptions(ConstraintViolationException ex) {
        StringBuilder errors = new StringBuilder();
        for(ConstraintViolation cv : ex.getConstraintViolations()) {
            errors.append(cv.getMessage()).append(System.lineSeparator());
        }
        return new ResponseMsg(MESSAGE, errors.toString(), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseMsg handleDBonstraintsViolations(DataIntegrityViolationException ex) {
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
        return new ResponseMsg(MESSAGE, errors.toString(), BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseMsg handleUnsupportedMethod(HttpRequestMethodNotSupportedException ex) {
        return new ResponseMsg(MESSAGE, ex.getMethod() + " request not supported.", BAD_REQUEST);
    }

}
