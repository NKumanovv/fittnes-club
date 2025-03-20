package com.fitness_club.web.dto;

import com.fitness_club.user.model.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 6, message = "Username must be at least 6 symbols")
    private String username;

    @Email
    private String email;

    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String password;

    @NotNull
    @Positive(message = "Must be a valid age")
    private int age;

    @NotNull
    @Positive(message = "Must be a valid height")
    private int height;

    @NotNull
    @Positive(message = "Must be a valid weight")
    private int weight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
