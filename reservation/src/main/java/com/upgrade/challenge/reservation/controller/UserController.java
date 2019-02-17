package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.EntityNotFoundException;
import com.upgrade.challenge.reservation.exception.ReservationException;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Created by fernando on 10/02/19.
 */
@RestController
@Validated
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/users/all")
    public List<User> findAll() throws UserException, EntityNotFoundException {
        return service.findAll();
    }

    @PostMapping("/users/save")
    public User save(@RequestBody User user) throws UserException {
        return service.save(user);
    }
}
