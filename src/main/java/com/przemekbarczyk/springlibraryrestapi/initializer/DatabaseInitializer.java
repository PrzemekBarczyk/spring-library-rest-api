package com.przemekbarczyk.springlibraryrestapi.initializer;

import com.przemekbarczyk.springlibraryrestapi.model.User;
import com.przemekbarczyk.springlibraryrestapi.model.UserRole;
import com.przemekbarczyk.springlibraryrestapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByEmail("admin@test.com").isEmpty()) {

            User admin = new User();
            admin.setFirstName("Jan");
            admin.setLastName("Kowalski");
            admin.setEmail("admin@test.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("qwerty"));
            admin.setRole(UserRole.ADMIN);

            userRepository.save(admin);
        }
    }
}
