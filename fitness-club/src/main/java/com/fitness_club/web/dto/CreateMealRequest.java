package com.fitness_club.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMealRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Positive(message = "Must be a valid number!")
    private Double calories;

    @Positive(message = "Must be a valid number!")
    private Double protein;

    @Positive(message = "Must be a valid number!")
    private Double carbs;

    @Positive(message = "Must be a valid number!")
    private Double fats;

    private boolean isPublic;
}
