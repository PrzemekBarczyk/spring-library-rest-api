package com.przemekbarczyk.springlibraryrestapi.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Firstname is empty or blank")
    private String firstName;

    @NotBlank(message = "Lastname is empty or blank")
    private String lastName;

    @NotBlank(message = "Email is empty or blank")
    private String email;

    @Size(min = 6, message = "Password has less than 6 characters")
    private String password;
}
