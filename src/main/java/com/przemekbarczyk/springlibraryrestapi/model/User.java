package com.przemekbarczyk.springlibraryrestapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Firstname is empty or blank")
    private String firstName;

    @NotBlank(message = "Lastname is empty or blank")
    private String lastName;

    @Column(name="email", unique = true)
    @NotBlank(message = "Email is empty or blank")
    private String email;

    @JsonIgnore
    @Size(min = 6, message = "Password has less than 6 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
