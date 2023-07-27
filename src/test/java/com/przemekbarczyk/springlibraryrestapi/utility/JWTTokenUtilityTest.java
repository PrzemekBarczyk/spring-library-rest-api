package com.przemekbarczyk.springlibraryrestapi.utility;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.przemekbarczyk.springlibraryrestapi.model.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JWTTokenUtilityTest {

    @Autowired
    private static JWTTokenUtility jwtTokenUtility;

    @BeforeAll
    public static void beforeAll() {
        jwtTokenUtility = new JWTTokenUtility();
        ReflectionTestUtils.setField(jwtTokenUtility, "secretKey", "lkj324jh89as7dfioj");
    }

    @Test
    public void givenCorrectUserDataWithEnumRole_whenEncodingAndDecoding_thenResultsAreTheSame() {

        Long userId = 1L;
        String userEmail = "ADMIN@test.com";
        UserRole userEnumRole = UserRole.ADMIN;

        String accessToken = jwtTokenUtility.generateAccessToken(userId, userEmail, userEnumRole.name());

        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(accessToken);

        Long decodedId = Long.valueOf(decodedJWT.getSubject());
        String decodedEmail = decodedJWT.getClaim("email").asString();
        String decodedRole = decodedJWT.getClaim("role").asString();

        assertEquals(userId, decodedId);
        assertEquals(userEmail, decodedEmail);
        assertEquals(userEnumRole.name(), decodedRole);
    }

    @Test
    public void givenCorrectUserDataWithStringRole_whenEncodingAndDecoding_thenResultsAreTheSame() {

        Long userId = 2L;
        String userEmail = "ReAdeR@test.com";
        String userStringRole = "rEaDeR";

        String accessToken = jwtTokenUtility.generateAccessToken(userId, userEmail, userStringRole);

        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(accessToken);

        Long decodedId = Long.valueOf(decodedJWT.getSubject());
        String decodedEmail = decodedJWT.getClaim("email").asString();
        String decodedRole = decodedJWT.getClaim("role").asString();

        assertEquals(userId, decodedId);
        assertEquals(userEmail, decodedEmail);
        assertEquals(userStringRole, decodedRole);
    }

    @Test
    public void givenUserDataWithNonExistentRole_whenEncodingAndDecoding_thenResultsAreTheSame() {

        Long userId = 1L;
        String userEmail = "hacker@test.com";
        String nonExistentUserStringRole = "HACKER";

        String accessToken = jwtTokenUtility.generateAccessToken(userId, userEmail, nonExistentUserStringRole);

        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(accessToken);

        Long decodedId = Long.valueOf(decodedJWT.getSubject());
        String decodedEmail = decodedJWT.getClaim("email").asString();
        String decodedRole = decodedJWT.getClaim("role").asString();

        assertEquals(userId, decodedId);
        assertEquals(userEmail, decodedEmail);
        assertEquals(nonExistentUserStringRole, decodedRole);
    }

    @Test
    public void givenUserDataWithNullId_whenEncoding_thenThrowException() {

        String userEmail = "reader@test.com";
        UserRole userEnumRole = UserRole.READER;

        assertThrows(NullPointerException.class, () ->
                jwtTokenUtility.generateAccessToken(null, userEmail, userEnumRole.name()));
    }

    @Test
    public void givenUserDataWithNullEmail_whenEncoding_thenResultsAreTheSame() {

        Long userId = 1L;
        UserRole userEnumRole = UserRole.READER;

        String accessToken = jwtTokenUtility.generateAccessToken(userId, null, userEnumRole.name());

        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(accessToken);

        Long decodedId = Long.valueOf(decodedJWT.getSubject());
        String decodedEmail = decodedJWT.getClaim("email").asString();
        String decodedRole = decodedJWT.getClaim("role").asString();

        assertEquals(userId, decodedId);
        assertNull(decodedEmail);
        assertEquals(userEnumRole.name(), decodedRole);
    }

    @Test
    public void givenUserDataWithNullRole_whenEncoding_thenResultsAreTheSame() {

        Long userId = 1L;
        String userEmail = "reader@test.com";

        String accessToken = jwtTokenUtility.generateAccessToken(userId, userEmail, null);

        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(accessToken);

        Long decodedId = Long.valueOf(decodedJWT.getSubject());
        String decodedEmail = decodedJWT.getClaim("email").asString();
        String decodedRole = decodedJWT.getClaim("role").asString();

        assertEquals(userId, decodedId);
        assertEquals(userEmail, decodedEmail);
        assertNull(decodedRole);
    }

    @Test
    public void givenNullAccessToken_whenDecoding_throwException() {

        assertThrows(JWTDecodeException.class, () ->
                jwtTokenUtility.decodeAccessToken(null));
    }

    @Test
    public void givenBlankAccessToken_whenDecoding_throwException() {

        assertThrows(JWTDecodeException.class, () ->
                jwtTokenUtility.decodeAccessToken(""));
    }

    @Test
    public void givenRandomAccessToken_whenDecoding_throwException() {
        assertThrows(JWTDecodeException.class, () ->
                jwtTokenUtility.decodeAccessToken("asdf.asdf.aasdf"));
    }
}
