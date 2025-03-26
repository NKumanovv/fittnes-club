package com.fitness_club.web.dto;

import com.fitness_club.user.model.Gender;
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
    @NotNull
    private String email;

    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String password;

    @Positive(message = "Must be a valid age")
    private int age;

    @Positive(message = "Must be a valid height")
    private int height;

    @Positive(message = "Must be a valid weight")
    private int weight;

    @NotNull
    private Gender gender;

}
