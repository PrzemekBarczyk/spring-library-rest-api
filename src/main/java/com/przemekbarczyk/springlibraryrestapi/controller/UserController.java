package com.przemekbarczyk.springlibraryrestapi.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.przemekbarczyk.springlibraryrestapi.model.User;
import com.przemekbarczyk.springlibraryrestapi.model.UserRole;
import com.przemekbarczyk.springlibraryrestapi.request.PasswordRequest;
import com.przemekbarczyk.springlibraryrestapi.request.UserRequest;
import com.przemekbarczyk.springlibraryrestapi.security.UserPrincipal;
import com.przemekbarczyk.springlibraryrestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/admin/users")
    public ResponseEntity<Page<User>> getSortedPageOfFilteredUsers(
            @RequestBody(required = false) User user,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "ascending") String sortDirection) {

        Page<User> users = userService.getSortedPageOfFilteredUsers(
                user,
                pageNumber, pageSize,
                sortBy, sortDirection);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/librarian/users/readers")
    public ResponseEntity<Page<User>> getSortedPageOfFilteredReaders(
            @RequestBody(required = false) User user,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "ascending") String sortDirection) {

        Page<User> users = userService.getSortedPageOfFilteredReaders(
                user,
                pageNumber, pageSize,
                sortBy, sortDirection);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }



    @GetMapping("/admin/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/librarian/user/reader/{id}")
    public ResponseEntity<User> getReaderById(@PathVariable Long id) {

        User user = userService.getReaderById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    @PostMapping("/admin/user/admin")
    public ResponseEntity<User> addAdmin(@RequestBody @Validated UserRequest adminRequest) {
        User addedAdmin = userService.addUser(adminRequest, UserRole.ADMIN);
        return new ResponseEntity<>(addedAdmin, HttpStatus.OK);
    }

    @PostMapping("/admin/user/librarian")
    public ResponseEntity<User> addLibrarian(@RequestBody @Validated UserRequest librarianRequest) {
        User addedLibrarian = userService.addUser(librarianRequest, UserRole.LIBRARIAN);
        return new ResponseEntity<>(addedLibrarian, HttpStatus.OK);
    }

    @PostMapping("/librarian/user/reader")
    public ResponseEntity<User> addReader(@RequestBody @Validated UserRequest readerRequest) {
        User addedReader = userService.addUser(readerRequest, UserRole.READER);
        return new ResponseEntity<>(addedReader, HttpStatus.OK);
    }



    @PutMapping("/admin/user/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody @Validated UserRequest newDataRequest) {

        User editedUser = userService.editUserById(id, newDataRequest);
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }

    @PutMapping("/librarian/user/reader/{id}")
    public ResponseEntity<User> editReader(@PathVariable Long id, @RequestBody @Validated UserRequest newDataRequest) {

        User editedUser = userService.editReaderById(id, newDataRequest);
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }

    @PatchMapping("/reader/user/logged/password")
    public ResponseEntity<User> editPassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Validated PasswordRequest passwordRequest) {

        User editedUser = userService.editPasswordById(principal.getId(), passwordRequest);
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }



    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {

        User deletedUser = userService.deleteUserById(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    @DeleteMapping("/librarian/user/reader/{id}")
    public ResponseEntity<User> deleteReaderById(@PathVariable Long id) {

        User deletedUser = userService.deleteReaderById(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }
}
