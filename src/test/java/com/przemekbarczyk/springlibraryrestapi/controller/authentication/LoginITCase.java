package com.przemekbarczyk.springlibraryrestapi.controller.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.przemekbarczyk.springlibraryrestapi.model.User;
import com.przemekbarczyk.springlibraryrestapi.model.UserRole;
import com.przemekbarczyk.springlibraryrestapi.repository.UserRepository;
import com.przemekbarczyk.springlibraryrestapi.response.LoginResponse;
import com.przemekbarczyk.springlibraryrestapi.utility.JWTTokenUtility;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginITCase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTTokenUtility jwtTokenUtility;

    @BeforeEach
    public void beforeEach() {

        userRepository.deleteAll();
        createUser("Piotr", "Kowalski", "admin@test.com", "qwerty", UserRole.ADMIN);
        createUser("Jan", "Nowak", "librarian@test.com", "qwerty", UserRole.LIBRARIAN);
        createUser("Anna", "Wiśniewska", "reader@test.com", "qwerty", UserRole.READER);
    }

    @Test
    public void givenAdmin_whenLogin_thenStatus200() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "admin@test.com");
        jsonRequest.put("password", "qwerty");

        // make request
        ResponseEntity<LoginResponse> loginResponse = restTemplate
                .postForEntity("/login", createRequestFromJSON(jsonRequest), LoginResponse.class);

        // check results
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());

        String accessToken = loginResponse.getBody().accessToken();
        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(accessToken);

        assertEquals("admin@test.com", decodedJWT.getClaim("email").asString());
        assertEquals("ADMIN", decodedJWT.getClaim("role").asString());
    }

    @Test
    public void givenLibrarian_whenLogin_thenStatus200() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "librarian@test.com");
        jsonRequest.put("password", "qwerty");

        // make request
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), LoginResponse.class);

        // check results
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());

        String accessToken = loginResponse.getBody().accessToken();
        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(accessToken);

        assertEquals("librarian@test.com", decodedJWT.getClaim("email").asString());
        assertEquals("LIBRARIAN", decodedJWT.getClaim("role").asString());
    }

    @Test
    public void givenReader_whenLogin_thenStatus200() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "reader@test.com");
        jsonRequest.put("password", "qwerty");

        // make request
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), LoginResponse.class);

        // check results
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());

        String accessToken = loginResponse.getBody().accessToken();
        DecodedJWT decodedJWT = jwtTokenUtility.decodeAccessToken(accessToken);

        assertEquals("reader@test.com", decodedJWT.getClaim("email").asString());
        assertEquals("READER", decodedJWT.getClaim("role").asString());
    }

    @Test
    public void givenNullBody_whenLogin_thenStatus403() {

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", null, String.class);

        // check results
        assertEquals(HttpStatus.FORBIDDEN, loginResponse.getStatusCode());
    }

    @Test
    public void givenNullEmail_whenLogin_thenStatus400() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("password", "qwerty");

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), String.class);

        // check results
        assertEquals(HttpStatus.BAD_REQUEST, loginResponse.getStatusCode());
    }

    @Test
    public void givenNullPassword_whenLogin_thenStatus400() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "reader@test.com");

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), String.class);

        // check results
        assertEquals(HttpStatus.BAD_REQUEST, loginResponse.getStatusCode());
    }

    @Test
    public void givenEmptyBody_whenLogin_thenStatus400() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "");
        jsonRequest.put("password", "");

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), String.class);

        // check results
        assertEquals(HttpStatus.BAD_REQUEST, loginResponse.getStatusCode());
    }

    @Test
    public void givenEmptyEmail_whenLogin_thenStatus400() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "");
        jsonRequest.put("password", "qwerty");

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), String.class);

        // check results
        assertEquals(HttpStatus.BAD_REQUEST, loginResponse.getStatusCode());
    }

    @Test
    public void givenEmptyPassword_whenLogin_thenStatus400() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "reader@test.com");
        jsonRequest.put("password", "");

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), String.class);

        // check results
        assertEquals(HttpStatus.BAD_REQUEST, loginResponse.getStatusCode());
    }

    @Test
    public void givenWrongEmailAndPassword_whenLogin_thenStatus401() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "reader2@test.com");
        jsonRequest.put("password", "qwerty2");

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), String.class);

        // check results
        assertEquals(HttpStatus.UNAUTHORIZED, loginResponse.getStatusCode());
    }

    @Test
    public void givenWrongEmail_whenLogin_thenStatus401() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "reader2@test.com");
        jsonRequest.put("password", "qwerty");

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), String.class);

        // check results
        assertEquals(HttpStatus.UNAUTHORIZED, loginResponse.getStatusCode());
    }

    @Test
    public void givenWrongPassword_whenLogin_thenStatus401() throws Exception {

        // create json request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("email", "reader@test.com");
        jsonRequest.put("password", "qwerty2");

        // make request
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/login", createRequestFromJSON(jsonRequest), String.class);

        // check results
        assertEquals(HttpStatus.UNAUTHORIZED, loginResponse.getStatusCode());
    }

    private void createUser(String firstName, String lastName, String email, String password, UserRole role) {

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));

        user.setRole(role);

        userRepository.save(user);
    }

    private HttpEntity<String> createRequestFromJSON(JSONObject json) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(json.toString(), headers);
    }
}
