package com.upgrade.challenge.reservation.controller;

import com.upgrade.challenge.reservation.domain.User;
import com.upgrade.challenge.reservation.exception.UserException;
import com.upgrade.challenge.reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created by fernando on 10/02/19.
 */
@RestController
@Validated
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/user/firstName")
    public User findByFirstName(@RequestParam(value="name") @NotEmpty String name) throws UserException {
        return service.findByFirstName(name);
    }

    @GetMapping("/user/lastName")
    public User findByLastName(@RequestParam(value="lastName") @NotEmpty String lastName) throws UserException {
        return service.findByLastName(lastName);
    }

    @GetMapping("/user/email")
    public User findByEmail(@RequestParam(value="email") @NotEmpty @Email String email) throws UserException {
        return service.findByEmail(email);
    }

    @PostMapping("/user/save")
    public User save(@RequestBody @Valid User user) throws UserException {
        return service.save(user);
    }

}
