package com.przemekbarczyk.springlibraryrestapi.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is empty or blank")
    private String email;

    @NotNull(message = "Password has less than 6 characters")
    @Size(min = 6, message = "Password has less than 6 characters")
    private String password;
}
