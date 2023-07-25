package com.przemekbarczyk.springlibraryrestapi.service;

import com.przemekbarczyk.springlibraryrestapi.mapper.UserMapper;
import com.przemekbarczyk.springlibraryrestapi.model.User;
import com.przemekbarczyk.springlibraryrestapi.model.UserRole;
import com.przemekbarczyk.springlibraryrestapi.repository.UserRepository;
import com.przemekbarczyk.springlibraryrestapi.request.PasswordRequest;
import com.przemekbarczyk.springlibraryrestapi.request.UserRequest;
import com.przemekbarczyk.springlibraryrestapi.utility.PagingUtility;
import com.przemekbarczyk.springlibraryrestapi.utility.SpecificationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> getSortedPageOfFilteredUsers(
            User user,
            Integer pageNumber, Integer pageSize,
            String sortBy, String sortDirection) {

        Specification<User> specification = new SpecificationUtility().getUserSpecification(user);

        Pageable paging = new PagingUtility().getPaging(pageNumber, pageSize, sortBy, sortDirection);

        Page<User> page = userRepository.findAll(specification, paging);

        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No users meets criteria");
        }

        return page;
    }

    public Page<User> getSortedPageOfFilteredReaders(
            User user,
            Integer pageNumber, Integer pageSize,
            String sortBy, String sortDirection) {

        if (user == null) {
            user = new User();
        }

        user.setRole(UserRole.READER);

        return getSortedPageOfFilteredUsers(user, pageNumber, pageSize, sortBy, sortDirection);
    }



    public User getUserById(Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id doesn't exist");
        }

        return user.get();
    }

    public User getReaderById(Long id) {

        Optional<User> user = userRepository.findByIdAndRole(id, UserRole.READER);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reader with this id doesn't exist");
        }

        return user.get();
    }

    public User getUserByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this email doesn't exist");
        }

        return user.get();
    }



    public User addUser(UserRequest userRequest, UserRole role) {

        UserMapper userMapper = new UserMapper();
        User user = userMapper.mapUserRequestToUser(userRequest);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(role);

        try {
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email isn't available");
        }
    }



    public User editUserById(Long id, UserRequest newData) {

        User user = getUserById(id);
        return editUser(user, newData);
    }

    public User editReaderById(Long id, UserRequest newDataRequest) {

        User user = getReaderById(id);
        return editUser(user, newDataRequest);
    }

    private User editUser(User user, UserRequest newDataRequest) {

        UserMapper userMapper = new UserMapper();
        User newDataUser = userMapper.mapUserRequestToUser(newDataRequest);

        user.setFirstName(newDataUser.getFirstName());
        user.setLastName(newDataUser.getLastName());
        user.setEmail(newDataUser.getEmail());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(newDataUser.getPassword()));

        try {
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email isn't available");
        }
    }

    public User editPasswordById(Long id, PasswordRequest passwordRequest) {

        User user = getUserById(id);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));

        return userRepository.save(user);
    }



    public User deleteUserById(Long id) {

        User user = getUserById(id);

        userRepository.deleteById(id);

        return user;
    }

    public User deleteReaderById(Long id) {

        User user = getReaderById(id);

        userRepository.deleteById(id);

        return user;
    }
}
