package com.przemekbarczyk.springlibraryrestapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @Min(value = 6, message = "Password has less than 6 characters")
    private String password;

    @NotBlank(message = "Role is empty or blank")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
