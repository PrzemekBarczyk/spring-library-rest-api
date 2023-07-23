package com.przemekbarczyk.springlibraryrestapi.service;

import com.przemekbarczyk.springlibraryrestapi.request.LoginRequest;
import com.przemekbarczyk.springlibraryrestapi.security.UserPrincipal;
import com.przemekbarczyk.springlibraryrestapi.utility.JWTTokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenUtility jwtTokenUtility;

    public String login(LoginRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            String role = principal.getAuthorities().iterator().next().getAuthority();

            return jwtTokenUtility.generateAccessToken(principal.getId(), principal.getEmail(), role);
        }
        catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}
