package com.fitness_club.web.dto;

import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class LoginRequest {

    @Size(min = 6, message = "Username must be at least 6 symbols!")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 symbols!")
    private String password;

}
