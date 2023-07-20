package com.przemekbarczyk.springlibraryrestapi.repository;

import com.przemekbarczyk.springlibraryrestapi.model.User;
import com.przemekbarczyk.springlibraryrestapi.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndRole(Long id, UserRole role);
}
