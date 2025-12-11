package com.fitness_club.web.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class WorkoutLogRequest {
    private UUID userId;
    private String workoutName;
    private int duration;
}