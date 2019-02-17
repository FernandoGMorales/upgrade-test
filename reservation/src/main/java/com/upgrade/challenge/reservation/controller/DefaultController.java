package com.upgrade.challenge.reservation.controller;


import com.upgrade.challenge.reservation.exception.format.ResponseMsg;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fernando on 15/02/19.
 */
@RestController
public class DefaultController implements ErrorController {

    @RequestMapping("/error")
    public ResponseMsg handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        return new ResponseMsg("Not a valid endpoint!", uri, statusCode);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
