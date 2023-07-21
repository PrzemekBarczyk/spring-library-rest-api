package com.przemekbarczyk.springlibraryrestapi.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTokenUtility {

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 100; // 24 h

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    public String generateAccessToken(Long userId, String email, String role) {
        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("email", email)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public DecodedJWT decodeAccessToken(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
    }
}
