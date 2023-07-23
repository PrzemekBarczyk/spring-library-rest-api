package com.przemekbarczyk.springlibraryrestapi.controller;

import com.przemekbarczyk.springlibraryrestapi.request.LoginRequest;
import com.przemekbarczyk.springlibraryrestapi.response.LoginResponse;
import com.przemekbarczyk.springlibraryrestapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request) {

        var token = authenticationService.login(request);
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }
}
