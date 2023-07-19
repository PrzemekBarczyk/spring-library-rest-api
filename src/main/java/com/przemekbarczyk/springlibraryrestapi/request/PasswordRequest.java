package com.przemekbarczyk.springlibraryrestapi.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordRequest {

    @Size(min = 6, message = "Password has less than 6 characters")
    private String password;
}
